package org.walkohm.resourcefilter

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSetContainer

class ResourceFilterExtension {
   String[] profiles = ["dev","release"]


}


class FilterPlugin implements Plugin<Project> {
    def pathToResources = 'src/main/resources/'
    @Override
    void apply(Project project) {
        def extension = project.extensions.create('resourceFilter', ResourceFilterExtension)


        def sourceSets = (SourceSetContainer) project.getProperties().get('sourceSets')
        project.afterEvaluate {
            extension.getProfiles().each { profile ->
                def profileDir = new File(pathToResources + profile + '/')
                if (!profileDir.exists())
                    profileDir.mkdir()


                project.task(profile + "Build") {
                    group = 'Profile'
                    doLast {
                        println profile.toUpperCase() + " BUILD"
                        println pathToResources
                       sourceSets.main.resources.srcDirs  = ["$pathToResources$profile/"]

                    }

                }.finalizedBy(project.tasks.getByName('build'))

                project.task(profile + "Run") {
                    group = 'Profile'
                    doLast {

                        println profile.toUpperCase() + " RUN"
                        sourceSets.main.resources.srcDirs = ["$pathToResources$profile/"]

                    }

                }.finalizedBy(project.tasks.getByName('run'))

            }

        }
    }
}
