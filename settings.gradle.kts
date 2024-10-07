pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        mavenLocal()
//        maven {
//            url = uri("https://company/com/maven2")
//        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        mavenLocal()
//        maven {
//            url = uri("https://company/com/maven2")
//        }

//        flatDir {
//            dirs "libs"
//        }

    }
}


rootProject.name = "Krishna"
include(":app")
 