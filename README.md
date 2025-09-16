# 🎯 Git's Code

> **GitHub 이벤트를 Discord로 알림받는 스프링 부트 기반 연동 서비스**

팀 협업에서 PR, Issue, Merge 이벤트를 놓치지 않도록  
GitHub → Discord 자동 알림을 지원합니다.

---

## 🚀 주요 기능
- **GitHub Webhook 연동**
  - Pull Request (opened, reopened, ready_for_review)
  - Issue (opened)
  - Push (main 브랜치)
- **Discord 알림 전송**
  - 봇을 통해 특정 채널에 이벤트 알림 메시지 전송
  - 메시지 템플릿 (제목, 작성자, URL 포함)
- **프로젝트 확장 가능**
  - 라벨 필터링, 템플릿 커스터마이징, 구독 관리 테이블 확장 가능

---

## 🛠️ 기술 스택
- **Backend**: Spring Boot 3.x (Java 17+)
- **Database**: PostgreSQL
- **Messaging**: Discord API (JDA)
- **Infra/Deploy**: Gradle, Railway (또는 Docker)
- **Etc**: GitHub Webhook, JSON 파싱(ObjectMapper)

---

## 📐 아키텍처
```mermaid
flowchart LR
  GH[GitHub Webhook] -->|이벤트 전달| API[Spring Boot API]
  API -->|메시지 생성| DISCORD[Discord Bot]
  API --> DB[(PostgreSQL)]
  DISCORD -->|알림| CH[Discord Channel]
