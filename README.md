# LightsaberShop

### Add secrets to get packages `settings.gradle`

``` gradle
pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://maven.pkg.github.com/Pandadika/LightsaberShop")
            credentials {
                username = "USERNAME"
                password = "TOKEN"
            }
        }
    }
}

rootProject.name = "LightsaberShop"
include ':app'
```

### Change default ip to the API
in `com.jedi.lightsabershop.activities.BaseActivity`
Set default
``` java
    ip = sharedPreferences.getString("ip", "192.168.1.200"); // set default ip here
    port = sharedPreferences.getInt("port", 8080); // set default port here
```
