# SMART DND ENFORCER FOR VOIP CALLS USING CONTEXT-AWARE DECISION ENGINE    
AI-Powered Intelligent VoIP Call Handling System for Do Not Disturb (DND) Mode  

“Smart DND Enforcer” is an intelligent Android-based system designed to ensure that important WhatsApp VoIP calls are not missed during Do Not Disturb mode by using context-aware decision making, cloud integration, and secure contact processing.

# Demo Link (UPLOADED IN LOOM)
DEMO:
https://www.loom.com/share/f93ad6e995bb43a3aec042c960d64f17 

# 📌 Project Description

Smart DND Enforcer is an AI-assisted Android application developed to solve one of the major limitations in modern smartphone Do Not Disturb (DND) systems — the inability to properly handle VoIP calls from applications such as WhatsApp. While traditional cellular calls from favorite contacts can bypass DND mode, VoIP calls often remain silent even for important contacts, leading to missed communications during emergencies and critical situations.

The project introduces an intelligent notification-based call handling mechanism that captures WhatsApp incoming call notifications in real time using Android’s NotificationListenerService. The system extracts caller information, preprocesses the data, and applies context-aware decision logic to determine whether the call should bypass DND mode.

To improve reliability and scalability, the project integrates cloud technologies using AWS services. Favorite contacts are securely synchronized to the cloud through Retrofit, API Gateway, AWS Lambda, and DynamoDB. Sensitive user data is protected using SHA-256 hashing before storage and transmission, ensuring secure and privacy-aware processing.

The system also considers contextual conditions such as call frequency and time-based scenarios to improve decision-making. If a call is identified as important, a custom ringtone is triggered using STREAM_ALARM so that the user is alerted even during DND mode.

The project was tested on multiple Android devices including Samsung and Redmi smartphones, where inconsistencies in default DND behavior for WhatsApp calls were observed. Smart DND Enforcer addresses this gap by providing a more reliable and intelligent call prioritization mechanism.


# Why Smart DND Enforcer?

- WhatsApp calls remain silent during DND mode even for favorite contacts
- Existing DND systems handle VoIP calls inconsistently across some devices
- Important emergency calls can be missed
- Notification formats differ across devices and applications
- Default DND systems lack contextual intelligence
- Existing systems can implement DND only for system calls.
- Users need intelligent and privacy-aware VoIP call handling


#  Features

## Core Features

- Real-Time WhatsApp Call Notification Detection
- Intelligent DND Bypass Mechanism
- Context-Aware Decision Making
- Favorite Contact Identification
- Custom Alarm Stream Ringtone Triggering
- Automatic Silent Handling for Non-Priority Calls
- Real-Time Notification Processing
- Cloud Synchronization Support


## Security Features

- SHA-256 Contact Hashing
- Secure Cloud Communication
- No Plain Text Storage of Sensitive Data
- Hashed Contact Synchronization
- Secure API-Based Data Transfer


## Cloud Features

- AWS API Gateway Integration
- AWS Lambda Processing
- DynamoDB Cloud Storage
- Retrofit-Based API Communication
- Scalable Cloud Architecture


#  How It Works

1. User grants notification and contact permissions
2. Starred contacts are extracted from the device
3. Contact names are preprocessed and hashed using SHA-256
4. Hashed contacts are stored in SQLite database
5. Contacts are synchronized to cloud using Retrofit
6. Incoming WhatsApp call notifications are captured
7. Caller name is extracted and normalized
8. Decision logic evaluates call importance
9. Time and repeated-call conditions are checked
10. If important, ringtone is triggered using STREAM_ALARM
11. Otherwise, the call remains silent


# Tech Stack Used

## Frontend / Mobile Application

- Android Studio
- Kotlin
- XML Layouts
- NotificationListenerService

## Database

- SQLite (Local Storage)
- AWS DynamoDB (Cloud Storage)

## Cloud Infrastructure

- AWS API Gateway
- AWS Lambda
- DynamoDB
- Retrofit API Communication

## Security

- SHA-256 Hashing
- Secure Data Transmission
- Hashed Contact Storage

# AWS Services Used

|   Service   |                  Purpose                   |
|-------------|--------------------------------------------|
| API Gateway | Receives requests from Android application |
| AWS Lambda  | Processes incoming cloud requests          |
| DynamoDB    | Stores hashed contact data securely        |


#  Modules Implemented

## Data Engineering
- Contact extraction
- Data preprocessing
- Duplicate removal
- SQLite data management

## AI Engineering
- Context-aware decision logic
- Call importance evaluation
- Frequency-based prioritization
- Time-aware processing

## Cloud Engineering
- Cloud synchronization
- API integration
- AWS service integration
- Serverless communication

## Security Engineering
- SHA-256 hashing
- Secure storage
- Secure cloud communication
- Privacy-aware processing

# 📋 Installation and Setup

## Prerequisites

- Android Studio
- Kotlin
- Android SDK
- AWS Account
- Internet Connection

## Local Setup

### Clone Repository

```bash
git clone https://github.com/YOUR_USERNAME/Smart-DND-Enforcer.git
```

### Open Project

```bash
Open project using Android Studio
```

### Configure AWS APIs

Add required API configurations and endpoints.

### Run Application

```bash
Run app on Android Emulator or Physical Device
```

# 🏷️ Hashtags

```text
#Android #AI #CloudComputing #AWS #CyberSecurity 
#Kotlin #VoIP #DND #NotificationListenerService 
#SQLite #DynamoDB #APIIntegration #SHA256
```
