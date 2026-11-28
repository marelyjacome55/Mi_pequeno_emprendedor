-- Permite iniciar sesión con nickname: resuelve el correo del usuario.
-- Ejecutar en el SQL Editor de Supabase.
create or replace function public.email_por_nickname(nick text)
returns table (correo text)
language sql
security definer
set search_path = public
as $$
  select u.email::text as correo
  from public."Usuario" us
  join auth.users u on u.id = us."auth_user_id"
  where lower(us."NIckName") = lower(nick)
  limit 1;
$$;

grant execute on function public.email_por_nickname(text) to anon, authenticated;
