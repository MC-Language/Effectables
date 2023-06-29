group = "tech.thatgravyboat"
version = "1.0"

plugins {
    base
}

val compileMcLang = task<Exec>("compileMcLang") {
    val sources: String by project
    commandLine("java", "-jar", "tools/mclang.jar", sources)
}

val createMetaFile = tasks.register("createMetaFile") {
    doLast {
        val packVersion: String by project
        val description: String by project
        file("mcbuild/pack.mcmeta")
                .writeText("""{"pack": {"pack_format": $packVersion, "description": "$description"}}""")
    }

    dependsOn(compileMcLang)
}

val buildMcLang = task<Zip>("buildMcLang") {
    from("mcbuild")
    destinationDirectory.set(file("build"))
    dependsOn(createMetaFile)
}

tasks.build {
    dependsOn(buildMcLang)
}