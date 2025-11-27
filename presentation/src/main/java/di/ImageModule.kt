package di

import coil3.ImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.memory.MemoryCache
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val imageModule = module {
    single {
        ImageLoader.Builder(androidContext())
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(get())
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(androidContext().cacheDir.resolve("image_cache"))
                    .maxSizePercent(0.02)
                    .build()
            }
            .build()
    }
}
