# voice-assistant-android

## Introduction

voice-assistant-android is an intelligent voice library middleware, encapsulating ASR (Automatic Speech Recognition), TTS (Text-to-Speech), WakeUp (Wake Word Detection), and LLM Chat smart voice interaction services SDK provided by various cloud service providers. This middleware aims to offer developers a simple and convenient interface, enabling them to effortlessly leverage these advanced voice interaction technologies without delving into the complexities of SDK integration and adaptation. With IVAssistant-Android, developers can focus more on crafting applications, unburdened by the intricacies of underlying technologies.

Currently supported capabilities:
- ASR Speech Recognition: Alibaba, Iflytek
- TTS Text-to-Speech: Alibaba (Chinese), Google (International)
- WakeUp Voice Activation: Picovoice (International)
- Chat Intelligent Chatting: OpenAI

## How to integrate

Add jitpack.io to your root build.gradle at the end of repositories:

```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
    }
}
```

Add the dependency:

```kotlin
// build.gradle.kts
// manditory
implementation("com.github.TW-Smart-CoE.voice-assistant-android:core:$latest_version")
// optional
implementation("com.github.TW-Smart-CoE.voice-assistant-android:alibabakit:$latest_version")
implementation("com.github.TW-Smart-CoE.voice-assistant-android:picovoicekit:$latest_version")
implementation("com.github.TW-Smart-CoE.voice-assistant-android:volcenginekit:$latest_version")
```

## Features

## Voice Synthesis
- [Ali Tts](AlibabaTts)

## Voice Recognition
- [Ali Asr](AlibabaAsr)

## Voice WakeUp
- [Picovoice WakeUp](PicovoiceWakeUp.md)

## Intelligent Chatting
- [OpenAI Chat](OpenAIChat)
