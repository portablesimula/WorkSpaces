package simula.plugin

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import simula.plugin.extensions.start_close.SimulaStartupActivity

class MyProjectActivityWrapper : ProjectActivity {
    override suspend fun execute(project: Project) {
        // Call your Java class method here
        SimulaStartupActivity.run(project)
    }
}