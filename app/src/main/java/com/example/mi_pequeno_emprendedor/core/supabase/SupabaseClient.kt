
package com.example.mi_pequeno_emprendedor.core.supabase

import com.example.mi_pequeno_emprendedor.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.auth.Auth

object SupabaseConfig {

    val client: SupabaseClient by lazy {
        require(BuildConfig.SUPABASE_URL.isNotBlank()) {
            "Falta configurar SUPABASE_URL en local.properties"
        }

        require(BuildConfig.SUPABASE_KEY.isNotBlank()) {
            "Falta configurar SUPABASE_KEY en local.properties"
        }

        createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_KEY
        ) {
            install(Auth)
            install(Postgrest)
        }
    }
}
