import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.triggers.vcs
import jetbrains.buildServer.configs.kotlin.vcs.GitVcsRoot

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2024.07"

project {

    vcsRoot(TeamcityRepoRoot)

    buildType(TeamcityRepo)

    params {
        password("teamcity.git.gitProxy.auth", "credentialsJSON:4a8fd896-2ff1-4613-882d-2bff21500ef8")
        param("teamcity.git.gitProxy.url", "https://git.jetbrains.team/~rpc")
        param("teamcity.internal.git.changesCollectionTimeLogging.enabled", "true")
    }
}

object TeamcityRepo : BuildType({
    name = "TeamcityRepo"

    vcs {
        root(TeamcityRepoRoot, "+:server-core", "+:react-ui")
    }

    triggers {
        vcs {
        }
    }
})

object TeamcityRepoRoot : GitVcsRoot({
    name = "TeamcityRepoRoot"
    url = "https://git.jetbrains.team/tc/TeamCity.git"
    branch = "master"
    branchSpec = "refs/heads/*"
    authMethod = password {
        userName = "ilya.voronin"
        password = "credentialsJSON:4a8fd896-2ff1-4613-882d-2bff21500ef8"
    }
})
