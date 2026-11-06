-- Registra una venta completa (Venta + detalle_venta) y descuenta stock.
-- Los montos se guardan en CENTAVOS (bigint).
-- Ejecutar en el SQL Editor de Supabase.
create or replace function public.registrar_venta(
  items jsonb,
  metodo text,
  efectivo bigint,
  cambio bigint
)
returns bigint
language plpgsql
security definer
set search_path = public
as $$
declare
  v_negocio bigint;
  v_venta bigint;
  v_total bigint := 0;
  v_num_dia smallint;
  r record;
begin
  if items is null or jsonb_array_length(items) = 0 then
    raise exception 'La venta no tiene productos';
  end if;

  select n."Negocio_id" into v_negocio
  from public."Negocio" n
  join public."Usuario" u on u."Usuario_id" = n.usuario_id
  where u."auth_user_id" = auth.uid()
  limit 1;

  if v_negocio is null then
    raise exception 'El usuario no tiene negocio';
  end if;

  for r in select value as item from jsonb_array_elements(items) loop
    if not exists (
      select 1
      from public."Producto/Servicio" p
      join public."Carpeta" c on c."Carpeta_id" = p.carpeta_id
      where p."Producto/Servicio_id" = (r.item->>'id')::bigint
        and c.negocio_id = v_negocio
    ) then
      raise exception 'Producto % no pertenece al negocio', r.item->>'id';
    end if;
    v_total := v_total + (r.item->>'subtotal')::bigint;
  end loop;

  select (coalesce(count(*), 0) + 1)::smallint into v_num_dia
  from public."Venta"
  where negocio_id = v_negocio and "Fecha"::date = current_date;

  insert into public."Venta" (
    "Monto Total", "Num_Venta_Dia", "Fecha", "Metodo de pago",
    "Efectivo recibido", "Cambio", negocio_id
  ) values (
    v_total, v_num_dia, now(), metodo, efectivo, cambio, v_negocio
  )
  returning "Venta_id" into v_venta;

  for r in select value as item from jsonb_array_elements(items) loop
    insert into public.detalle_venta (
      venta_id, producto_servicio_id, nombre_producto,
      cantidad_vendida, precio_unitario
    ) values (
      v_venta,
      (r.item->>'id')::bigint,
      r.item->>'nombre',
      (r.item->>'cantidad')::numeric,
      (r.item->>'precio')::numeric
    );

    update public."Producto/Servicio"
    set "Stock" = coalesce("Stock", 0) - (r.item->>'cantidad')::int,
        "Fecha Actualizacion" = now()
    where "Producto/Servicio_id" = (r.item->>'id')::bigint;
  end loop;

  return v_venta;
end;
$$;

grant execute on function public.registrar_venta(jsonb, text, bigint, bigint) to authenticated;
