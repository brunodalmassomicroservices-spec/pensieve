# System Specification (Spec): Spaced Repetition App

## 1. Visão Geral
Sistema de aprendizagem por repetição espaçada focado em ativação de memória via palavras-chave (gatilhos). O objetivo do app é proporcionar revisões diárias ultrarrápidas sem sobrecarga cognitiva.

---

## 2. Regras de Negócio (Business Rules)
* **RN-01 (Criação de Gatilho):** No Dia 0, ao criar um `Trigger`, o sistema DEVE gerar automaticamente 4 registros de `Review` associados com os intervalos fixos:
  * `D+1` (`created_at` + 1 dia)
  * `D+7` (`created_at` + 7 dias)
  * `D+30` (`created_at` + 30 dias)
  * `D+180` (`created_at` + 180 dias)
* **RN-02 (Sucesso na Revisão):** Ao marcar `COMPLETED` (Lembrei), a revisão atual é fechada e a próxima revisão agendada permanece inalterada.
* **RN-03 (Falha na Revisão):** Ao marcar `FAILED` (Esqueci), o sistema altera a revisão atual para `FAILED` e gera um novo agendamento emergencial para `D+1` (`CURRENT_DATE + 1 dia`) para reancorar o conceito.
* **RN-04 (Consolidação Diária):** A consulta de revisões do dia deve retornar todos os itens com `status = 'PENDING'` e `scheduled_for <= CURRENT_DATE`.

---

## 3. Modelo de Dados (Domain Schema)

### Entidades

```dbml
Enum review_status {
  PENDING
  COMPLETED
  FAILED
  SKIPPED
}

Table users {
  id uuid [pk]
  name varchar
  email varchar [unique]
  created_at timestamp
}

Table subjects {
  id uuid [pk]
  user_id uuid [ref: > users.id]
  title varchar
  color_hex varchar
  created_at timestamp
}

Table triggers {
  id uuid [pk]
  subject_id uuid [ref: > subjects.id]
  title varchar [note: 'Palavra-chave/Gatilho principal']
  notes text [note: 'Anotação opcional/Explicação']
  created_at timestamp
}

Table reviews {
  id uuid [pk]
  trigger_id uuid [ref: > triggers.id]
  interval_days int [note: '1, 7, 30, 180']
  scheduled_for date
  completed_at timestamp
  status review_status
  created_at timestamp
}

## 4. Contratos de API (REST Endpoints)

### POST /api/v1/triggers
	* **Request:
	{
	  "subject_id": "uuid",
	  "title": "Idempotência",
	  "notes": "Garantir o mesmo resultado independentemente de quantas vezes a operação for executada."
	}
	
	* **Response (201 Created):
	{
	  "id": "uuid-trigger",
	  "title": "Idempotência",
	  "created_reviews_count": 4
	}
	
### GET /api/v1/reviews/today
	* ** Response (200 OK):
	{
	  "total_pending": 2,
	  "items": [
		{
		  "review_id": "uuid-review-1",
		  "trigger_id": "uuid-trigger-1",
		  "subject_name": "Microservices",
		  "trigger_title": "Idempotência",
		  "notes": "Garantir o mesmo resultado...",
		  "interval_days": 1
		}
	  ]
	}
	
### POST /api/v1/reviews/{id}/evaluate
	* ** Request:
	{
	  "result": "REMEMBERED" // ou "FORGOTTEN"
	}

	* ** Response (200 OK):
	{
	  "review_id": "uuid-review-1",
	  "status": "COMPLETED",
	  "next_review_date": "2026-08-26"
	}

## 5. Máquina de Estados da UI (Modo Foco)
	* ** IDLE: Exibe card com trigger_title centralizado + progresso diário (X/Y).
	* ** REVEALED: Ao clicar/tocar no card, revela notes.
	* ** EVALUATED: Usuário escolhe Lembrei (Swipe Right / Tecla 2) ou Esqueci (Swipe Left / Tecla 1).
	* ** NEXT: Transiciona suavemente para a próxima Review pendente.