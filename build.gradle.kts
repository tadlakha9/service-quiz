import com.soprabanking.dxp.dok.gradle.configureDok
import com.soprabanking.dxp.dxp
import com.soprabanking.dxp.lib

val commonsBankingVersion by extra { "1.1.0-RC9" }

dxpJavaApplication {
    apiVersion = 1
}
dependencies {
    implementation(dxp("api"))
    implementation(dxp("security"))
    implementation(dxp("mongodb"))
    runtimeOnly(dxp("monitor"))
    testImplementation(dxp("test"))
    implementation(lib("banking", "security-host-user"))
    testImplementation(lib("banking", "security-host-user-test"))
    testImplementation(lib("banking", "security-tenant-user-test"))
}

// For 'Document my API' chapter
configureDok {
    description = "This is DxP's quickstart application. It handles quizzes."
    tagsDescription = setOf("quiz" to "A quiz")
}