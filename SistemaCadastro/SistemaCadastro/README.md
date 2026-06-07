# Sistema de Cadastro de Clientes e Contatos

## Estrutura do Projeto
```
SistemaCadastro/
├── Main.java          # Arquivo principal com todo o sistema
├── clientes.csv       # Dados de teste - clientes
├── contatos.csv       # Dados de teste - contatos
└── README.md
```

## Como compilar e executar
```bash
# Compilar (na pasta SistemaCadastro):
javac Main.java

# Executar (na mesma pasta onde estão os arquivos .csv):
java Main
```

## Estrutura das Matrizes

### matrizClientes — String[n][8]
| Índice | Campo           |
|--------|----------------|
| [0]    | codigo          |
| [1]    | nome            |
| [2]    | cpf_cnpj        |
| [3]    | data_nascimento |
| [4]    | sexo            |
| [5]    | cidade          |
| [6]    | estado          |
| [7]    | status          |

### matrizContatos — String[n][5]
| Índice | Campo           |
|--------|----------------|
| [0]    | codigo_contato  |
| [1]    | codigo_cliente  |
| [2]    | tipo            |
| [3]    | valor           |
| [4]    | status          |

## Funcionalidades implementadas

### Clientes
- Incluir cliente (código gerado automaticamente)
- Listar todos os clientes
- Consultar cliente por código (exibe contatos vinculados)
- Alterar dados do cliente
- Apagar cliente (remove contatos vinculados automaticamente)
- Ordenar clientes por nome (Bubble Sort manual)

### Contatos
- Incluir contato vinculado a um cliente
- Listar contatos de um cliente
- Alterar contato
- Apagar contato

## Regras técnicas respeitadas
- Sem variáveis globais — matrizes passadas sempre por parâmetro
- Sem ArrayList, banco de dados ou arquivos de persistência
- Crescimento manual de matrizes (aumentarMatrizClientes / aumentarMatrizContatos)
- Ordenação manual por Bubble Sort sem métodos prontos
- Comparação de strings manual sem compareTo() ou equals() nativos de ordenação
- Relação 1:N (cliente → contatos) — ao apagar cliente, contatos são apagados
