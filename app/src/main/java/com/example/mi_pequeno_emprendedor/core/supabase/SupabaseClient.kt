package com.example.mi_pequeno_emprendedor.core.supabase

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseConfig {

    val client: SupabaseClient by lazy {
        createSupabaseClient(
            supabaseUrl = "TU_URL_DE_SUPABASE",
            supabaseKey = "TU_CLAVE_PUBLICABLE"
        ) {
            install(Postgrest)
        }
    }
}