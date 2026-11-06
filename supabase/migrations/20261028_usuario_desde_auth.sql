-- Copia cada usuario de auth.users a public."Usuario" usando los metadatos
-- que envía la app en signUp (nombre, apellido, nickname, fecha_nacimiento).
-- Ejecutar en el SQL Editor de Supabase.

create or replace function public.crear_usuario_desde_auth()
returns trigger
language plpgsql
security definer
set search_path = public
as $$
begin
  insert into public."Usuario" (
    "Nombre",
    "Apellidos",
    "NIckName",
    "Fecha nacimiento",
    "auth_user_id"
  )
  values (
    coalesce(nullif(new.raw_user_meta_data ->> 'nombre', ''), split_part(new.email, '@', 1)),
    nullif(new.raw_user_meta_data ->> 'apellido', ''),
    nullif(new.raw_user_meta_data ->> 'nickname', ''),
    nullif(new.raw_user_meta_data ->> 'fecha_nacimiento', '')::date,
    new.id
  )
  on conflict ("auth_user_id") do nothing;

  return new;
end;
$$;

drop trigger if exists on_auth_user_created on auth.users;

create trigger on_auth_user_created
  after insert on auth.users
  for each row execute function public.crear_usuario_desde_auth();

-- Rellena los usuarios que ya se registraron antes del trigger.
insert into public."Usuario" ("Nombre", "Apellidos", "NIckName", "Fecha nacimiento", "auth_user_id")
select
  coalesce(nullif(u.raw_user_meta_data ->> 'nombre', ''), split_part(u.email, '@', 1)),
  nullif(u.raw_user_meta_data ->> 'apellido', ''),
  nullif(u.raw_user_meta_data ->> 'nickname', ''),
  nullif(u.raw_user_meta_data ->> 'fecha_nacimiento', '')::date,
  u.id
from auth.users u
where not exists (
  select 1 from public."Usuario" us where us."auth_user_id" = u.id
);
