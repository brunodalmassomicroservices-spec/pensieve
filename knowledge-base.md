```markdown
# Knowledge Base (KB): Contexto, Arquitetura e Glossário

## 1. Glossário do Domínio
* **Gatilho (Trigger):** Palavra ou termo curto que funciona como gancho para ativar a memória sem a necessidade de ler um texto explicativo longo.
* **Evocação Ativa (Active Recall):** Prática de recuperar a informação da memória intencionalmente ao ver apenas a palavra-gatilho.
* **Curva do Esquecimento:** Teoria de Hermann Ebbinghaus demonstrando o declínio de retenção de memória ao longo do tempo se não houver revisões.
* **Dia 0:** O momento do estudo inicial em que a informação é sintetizada e o gatilho é cadastrado.

---

## 2. Decisões de Arquitetura (ADRs)

### ADR-001: Intervalos Fixos vs. Algoritmo Dinâmico (Ex: SuperMemo/Anki)
* **Status:** Aceito.
* **Contexto:** Algoritmos adaptativos exigem pontuações de facilidade (Ease Factor) e cálculo contínuo de matrizes de repetição.
* **Decisão:** Utilizar régua fixa de **1, 7, 30 e 180 dias**.
* **Justificativa:** Reduz a complexidade técnica no MVP, torna o comportamento do sistema previsível para o usuário e atende com alta precisão o objetivo de consolidação de médio/longo prazo.

### ADR-002: Suporte Offline-First para Dispositivos Móveis
* **Status:** Aceito.
* **Contexto:** A sessão diária precisa ser rápida e funcionar sem dependência de latência de rede.
* **Decisão:** Armazenar a fila de revisões do dia localmente (ex: SQLite via Drift/WatermelonDB) e sincronizar em segundo plano.

---

## 3. Requisitos Não-Funcionais (NFRs)
* **Desempenho de Renderização:** O tempo de transição entre cartões no Modo Foco deve ser inferior a **100ms**.
* **Disparo de Push Notification:** O job de notificação diária deve rodar às **08:00 (horário local do usuário)** apenas se houver revisões pendentes com `scheduled_for <= CURRENT_DATE`.
* **Escalabilidade de Banco:** Índices compostos obrigatórios nas colunas `(scheduled_for, status)` da tabela `reviews` para suportar buscas rápidas em massa.