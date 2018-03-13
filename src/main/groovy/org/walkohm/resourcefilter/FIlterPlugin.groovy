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
        def srcDirs = sourceSets.getByName('main').getResources().getSrcDirs()
        project.afterEvaluate {
            extensions.getProfiles().each {profile->

             def profileDir = new File(pathToResources+ profile + '/')
                if(!profileDir.exists())
                profileDir.mkdir()
                project.tasks.create( profile +  "Build") {
                    doLast {
                        println profile.toUpperCase() +  " BUILD"
                        srcDirs = [pathToResources+ profile + '/']

                 }

                }.finalizedBy(project.tasks.getByName('build'))
                project.tasks.create(profile + "Run") {
                    doLast {
                        println profile.toUpperCase() +  " RUN"
                        srcDirs = [pathToResources+ profile + '/']

                 }

                }.finalizedBy(project.tasks.getByName('run'))

            }
        }
    }
}
