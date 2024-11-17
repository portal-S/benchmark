plugins {
    id("java");
    id ("me.champeau.jmh") version "0.7.2"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.test {
    useJUnitPlatform()
}

jmh {
    warmupIterations = 0
    iterations = 1
    fork = 1
    benchmarkMode.add("avgt")
    threads = 4
    timeOnIteration.set("1s")
    jmhTimeout.set("1s")
}