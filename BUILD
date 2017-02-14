load("//tools/bzl:junit.bzl", "junit_tests")
load(
    "//tools/bzl:plugin.bzl",
    "gerrit_plugin",
    "PLUGIN_DEPS",
    "PLUGIN_TEST_DEPS",
)

gerrit_plugin(
    name = "sync-events",
    srcs = glob(["src/main/java/**/*.java"]),
    resources = glob(["src/main/resources/**/*"]),
    manifest_entries = [
        "Gerrit-PluginName: sync-events",
        "Gerrit-Module: com.ericsson.gerrit.plugins.syncevents.Module",
        "Gerrit-HttpModule: com.ericsson.gerrit.plugins.syncevents.HttpModule",
        "Implementation-Title: sync-events plugin",
        "Implementation-URL: https://gerrit-review.googlesource.com/#/admin/projects/plugins/sync-events",
    ],
)

junit_tests(
    name = "sync_events_tests",
    srcs = glob(["src/test/java/**/*.java"]),
    tags = ["sync-events"],
    deps = PLUGIN_DEPS + PLUGIN_TEST_DEPS + [
        ":sync-events__plugin",
        "@mockito//jar",
        "@wiremock//jar",
    ],
)
