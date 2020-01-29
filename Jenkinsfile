#!/usr/bin/env groovy
def config = [
    scriptVersion              : 'feature/aos-3695-detekt',
    iqOrganizationName         : 'Team AOS',
    pipelineScript             : 'https://git.aurora.skead.no/scm/ao/aurora-pipeline-scripts.git',
    downstreamSystemtestJob    : [branch: env.BRANCH_NAME],
    credentialsId              : "github",
    javaVersion                : 11,
    nodeVersion                : '10',
    jiraFiksetIKomponentversjon: true,
    chatRoom                   : "#aos-notifications",
    compileProperties          : "-U",
    detektBaselinePath         : "/target/detekt.xml",
    detektConfigPath           : "/default-detekt-config.yml",
    versionStrategy            : [
        [branch: 'master', versionHint: '1']
    ]
]
fileLoader.withGit(config.pipelineScript, config.scriptVersion) {
  jenkinsfile = fileLoader.load('templates/leveransepakke')
}
jenkinsfile.run(config.scriptVersion, config)
