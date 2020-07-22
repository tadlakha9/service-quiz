rootProject.name = "service-quiz"

rootProject.apply { name = settings.startParameter.projectProperties["appName"] ?: name }