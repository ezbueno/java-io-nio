# Projeto IO vs NIO - Java

## 📋 Descrição

Este projeto demonstra a diferença entre as APIs de **IO (Input/Output)** tradicional e **NIO (New Input/Output)** em Java, desenvolvido durante o bootcamp **TONNIE - Java and AI in Europe** em parceria com a **Digital Innovation ONE (DIO)**.

O projeto implementa operações de persistência de dados em arquivos CSV utilizando ambas as abordagens, permitindo comparar performance, usabilidade e características de cada API.

## 🏗️ Estrutura do Projeto

```
📁 managedFiles/
├── 📁 IO/
│   └── user.csv
├── 📁 NIO/
│   └── user.csv
📁 src/
└── 📁 main/
    └── 📁 java/
        └── 📁 developer.ezandro/
            ├── 📁 exception/
            │   └── FilePersistenceException.java
            ├── 📁 main/
            │   ├── IOMain.java
            │   └── NIOMain.java
            └── 📁 persistence/
                ├── FilePersistence.java
                ├── IOFilePersistence.java
                └── NIOFilePersistence.java
```

## 🔧 Componentes Principais

### 📦 Pacote `exception`
- **FilePersistenceException**: Exceção customizada para tratar erros específicos de persistência de arquivos

### 📦 Pacote `persistence`
- **FilePersistence**: Interface que define o contrato para operações de persistência
- **IOFilePersistence**: Implementação usando API IO tradicional (java.io)
- **NIOFilePersistence**: Implementação usando API NIO (java.nio)

### 📦 Pacote `main`
- **IOMain**: Classe principal demonstrando uso da API IO tradicional
- **NIOMain**: Classe principal demonstrando uso da API NIO

## 🆚 IO vs NIO - Principais Diferenças

### IO Tradicional (java.io)
- **Orientado a Stream**: Trabalha com fluxos de dados sequenciais
- **Bloqueante**: Operações bloqueiam a thread até completarem
- **Simples**: API mais simples e intuitiva
- **Melhor para**: Operações simples e arquivos pequenos

### NIO (java.nio)
- **Orientado a Buffer**: Trabalha com buffers de dados
- **Não-bloqueante**: Permite operações assíncronas
- **Canais**: Utiliza channels para operações mais eficientes
- **Melhor para**: Operações com arquivos grandes e alta performance

## 🚀 Como Executar

### Executando com IO Tradicional
```bash
java developer.ezandro.main.IOMain
```

### Executando com NIO
```bash
java developer.ezandro.main.NIOMain
```

## 📊 Dados de Exemplo

O projeto trabalha com arquivos CSV contendo dados de usuários separados por ponto e vírgula. Os dados são gerados dinamicamente pelas classes main através de operações de inserção, remoção e substituição:

### Processo IOMain:
- **Inserções iniciais**: Lucas, Maria, João
- **Operações**: Remoções por padrão de data, buscas por nome/email, substituição de registro
- **Estado final**: Carlos e Maria (após operações de replace e remove)

### Processo NIOMain:
- **Inserções iniciais**: Bianca, Bernardo, Ricardo
- **Operações**: Remoções por padrão de data, buscas por nome/email, substituição de registro
- **Estado final**: Bianca (após operações de replace e remove)

### Arquivos resultantes:

**IO/user.csv:**
```csv
Carlos;carlos@carlos.com;22/03/1991;
Maria;maria@maria.com;23/10/2000;
```

**NIO/user.csv:**
```csv
Bianca;bianca@bianca.com;22/03/1998;
```

**Formato**: `nome;email;data_nascimento;`

## 🛠️ Funcionalidades Implementadas

- ✅ **Inserção de dados** em arquivos CSV
- ✅ **Leitura completa** de arquivos (findAll)
- ✅ **Busca por padrões** específicos (findBy)
- ✅ **Remoção de registros** por padrão de texto
- ✅ **Substituição de registros** por padrão de match
- ✅ **Tratamento de exceções** customizado
- ✅ **Comparação de performance** entre IO e NIO
- ✅ **Estrutura modular** e extensível
- ✅ **Operações dinâmicas** de manipulação de dados

## 🎯 Objetivos de Aprendizado

1. **Compreender** as diferenças entre IO e NIO
2. **Implementar** soluções usando ambas as APIs
3. **Comparar** performance e usabilidade
4. **Aplicar** boas práticas de tratamento de exceções
5. **Desenvolver** código limpo e bem estruturado

## 📚 Conceitos Abordados

- **Streams vs Buffers**
- **Blocking vs Non-blocking I/O**
- **Channels e Selectors**
- **File handling em Java**
- **Exception handling customizado**
- **Design Patterns (Strategy Pattern)**

## 🏆 Bootcamp TONNIE

Este projeto foi desenvolvido como parte do bootcamp **TONNIE - Java and AI in Europe**, uma parceria entre a **Digital Innovation ONE (DIO)** e organizações europeias, focando em:

- Desenvolvimento Java moderno
- Integração com tecnologias de IA
- Práticas de desenvolvimento europeu
- Networking internacional

## 📖 Recursos Adicionais

- [Documentação Oracle - Java IO](https://docs.oracle.com/javase/tutorial/essential/io/)
- [Documentação Oracle - Java NIO](https://docs.oracle.com/javase/tutorial/essential/io/fileio.html)
- [DIO - Digital Innovation ONE](https://www.dio.me/)

## 👨‍💻 Desenvolvedor

**Ezandro Bueno** - Participante do Bootcamp TONNIE
- Bootcamp: Java and AI in Europe
- Plataforma: Digital Innovation ONE (DIO)

---

*Desenvolvido com ❤️ durante o bootcamp TONNIE - Java and AI in Europe*
