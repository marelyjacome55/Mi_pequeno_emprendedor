-- Devuelve el inventario del negocio del usuario autenticado.
-- Ejecutar en el SQL Editor de Supabase.
create or replace function public.mis_productos()
returns table (
  id bigint,
  nombre text,
  precio integer,
  stock integer,
  detalle text
)
language sql
security definer
set search_path = public
as $$
  select
    p."Producto/Servicio_id" as id,
    p."Nombre" as nombre,
    coalesce(p."Precio de venta", 0) as precio,
    coalesce(p."Stock", 0) as stock,
    coalesce(p."Cantidad", '') as detalle
  from public."Producto/Servicio" p
  join public."Carpeta" c on c."Carpeta_id" = p.carpeta_id
  join public."Negocio" n on n."Negocio_id" = c.negocio_id
  join public."Usuario" u on u."Usuario_id" = n.usuario_id
  where u."auth_user_id" = auth.uid()
  order by p."Nombre";
$$;

grant execute on function public.mis_productos() to authenticated;

-- Nombre del negocio del usuario autenticado (para el ticket).
create or replace function public.mi_negocio()
returns table (nombre text)
language sql
security definer
set search_path = public
as $$
  select n."Nombre" as nombre
  from public."Negocio" n
  join public."Usuario" u on u."Usuario_id" = n.usuario_id
  where u."auth_user_id" = auth.uid()
  limit 1;
$$;

grant execute on function public.mi_negocio() to authenticated;
