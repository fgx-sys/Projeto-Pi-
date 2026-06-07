import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Sistema de Cadastro de Clientes e Contatos
 *
 * Estrutura de dados:
 * - Matriz de Clientes: String[linhas][8 colunas]
 *   [0] codigo | [1] nome | [2] cpf_cnpj | [3] data_nascimento
 *   [4] sexo   | [5] cidade | [6] estado | [7] status
 *
 * - Matriz de Contatos: String[linhas][5 colunas]
 *   [0] codigo_contato | [1] codigo_cliente | [2] tipo | [3] valor | [4] status
 *
 * Regras técnicas:
 * - Sem variáveis globais — matrizes passadas sempre por parâmetro
 * - Sem ArrayList, banco de dados ou arquivos de persistência
 * - Crescimento manual de matrizes (aumentarMatrizClientes / aumentarMatrizContatos)
 * - Ordenação manual por Bubble Sort sem métodos prontos
 * - Comparação de strings manual (compararNomeCharPorChar)
 * - Relação 1:N (cliente → contatos)
 */
public class Main {

    // =====================================================================
    // MÉTODO PRINCIPAL
    // =====================================================================

    public static void main(String[] args) {
        String[][] matrizClientes = new String[0][8];
        String[][] matrizContatos = new String[0][5];

        // Carrega dados de teste automaticamente ao iniciar
        System.out.println("================================================");
        System.out.println("  SISTEMA DE CADASTRO DE CLIENTES E CONTATOS   ");
        System.out.println("================================================");
        System.out.println("Carregando dados de teste...");
        matrizClientes = carregarClientesCSV(matrizClientes);
        matrizContatos = carregarContatosCSV(matrizContatos);
        System.out.println("================================================");

        Object[] resultado = menuPrincipal(matrizClientes, matrizContatos);
        matrizClientes = (String[][]) resultado[0];
        matrizContatos = (String[][]) resultado[1];

        System.out.println("\nSistema encerrado. Até logo!");
    }

    // =====================================================================
    // MENUS
    // =====================================================================

    /**
     * Menu principal — retorna Object[]{matrizClientes, matrizContatos} ao encerrar.
     */
    private static Object[] menuPrincipal(String[][] matrizClientes, String[][] matrizContatos) {
        Scanner scanner = new Scanner(System.in);
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n========== MENU PRINCIPAL ==========");
            System.out.println("1 - Gerenciar Clientes");
            System.out.println("2 - Gerenciar Contatos");
            System.out.println("3 - Relatórios");
            System.out.println("0 - Sair");
            System.out.println("=====================================");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine().trim());

                switch (opcao) {
                    case 1:
                        Object[] resC = menuClientes(scanner, matrizClientes, matrizContatos);
                        matrizClientes = (String[][]) resC[0];
                        matrizContatos = (String[][]) resC[1];
                        break;

                    case 2:
                        matrizContatos = menuContatos(scanner, matrizClientes, matrizContatos);
                        break;

                    case 3:
                        menuRelatorios(scanner, matrizClientes, matrizContatos);
                        break;

                    case 0:
                        System.out.println("\nEncerrando sistema...");
                        break;

                    default:
                        System.out.println("\nOpção inválida! Tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\nEntrada inválida! Digite apenas números.");
                opcao = -1;
            }
        }

        scanner.close();
        return new Object[]{matrizClientes, matrizContatos};
    }

    // ------------------------------------------------------------------

    /**
     * Menu de Clientes — retorna Object[]{matrizClientes, matrizContatos}
     */
    private static Object[] menuClientes(Scanner scanner,
                                         String[][] matrizClientes,
                                         String[][] matrizContatos) {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n========== MENU CLIENTES ==========");
            System.out.println("1 - Incluir Cliente");
            System.out.println("2 - Listar Clientes");
            System.out.println("3 - Consultar Cliente por Código");
            System.out.println("4 - Alterar Cliente");
            System.out.println("5 - Apagar Cliente");
            System.out.println("6 - Ordenar por Nome");
            System.out.println("0 - Voltar");
            System.out.println("====================================");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine().trim());

                switch (opcao) {
                    case 1:
                        matrizClientes = incluirCliente(scanner, matrizClientes);
                        break;
                    case 2:
                        listarClientesTabela(matrizClientes);
                        break;
                    case 3:
                        consultarCliente(scanner, matrizClientes, matrizContatos);
                        break;
                    case 4:
                        matrizClientes = alterarCliente(scanner, matrizClientes);
                        break;
                    case 5:
                        Object[] res = apagarCliente(scanner, matrizClientes, matrizContatos);
                        matrizClientes = (String[][]) res[0];
                        matrizContatos = (String[][]) res[1];
                        break;
                    case 6:
                        matrizClientes = ordenarClientesPorNome(matrizClientes);
                        System.out.println("Clientes ordenados por nome com sucesso!");
                        listarClientesTabela(matrizClientes);
                        break;
                    case 0:
                        System.out.println("Voltando ao menu principal...");
                        break;
                    default:
                        System.out.println("Opção inválida!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida! Digite apenas números.");
                opcao = -1;
            }
        }

        return new Object[]{matrizClientes, matrizContatos};
    }

    // ------------------------------------------------------------------

    /**
     * Menu de Contatos — retorna matrizContatos atualizada
     */
    private static String[][] menuContatos(Scanner scanner,
                                           String[][] matrizClientes,
                                           String[][] matrizContatos) {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n========== MENU CONTATOS ==========");
            System.out.println("1 - Incluir Contato");
            System.out.println("2 - Listar Contatos (Todos os clientes)");
            System.out.println("3 - Listar Contatos de um Cliente");
            System.out.println("4 - Alterar Contato");
            System.out.println("5 - Apagar Contato");
            System.out.println("0 - Voltar");
            System.out.println("====================================");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine().trim());

                switch (opcao) {
                    case 1:
                        matrizContatos = incluirContato(scanner, matrizClientes, matrizContatos);
                        break;
                    case 2:
                        listarContatosTabela(matrizClientes, matrizContatos);
                        break;
                    case 3:
                        listarContatosPorCliente(scanner, matrizClientes, matrizContatos);
                        break;
                    case 4:
                        matrizContatos = alterarContato(scanner, matrizClientes, matrizContatos);
                        break;
                    case 5:
                        matrizContatos = apagarContato(scanner, matrizClientes, matrizContatos);
                        break;
                    case 0:
                        System.out.println("Voltando ao menu principal...");
                        break;
                    default:
                        System.out.println("Opção inválida!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida! Digite apenas números.");
                opcao = -1;
            }
        }

        return matrizContatos;
    }

    // ------------------------------------------------------------------

    /**
     * Menu de Relatórios
     */
    private static void menuRelatorios(Scanner scanner,
                                       String[][] matrizClientes,
                                       String[][] matrizContatos) {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n========== MENU RELATÓRIOS ==========");
            System.out.println("1 - Clientes e Total de Contatos por Cliente");
            System.out.println("2 - Sumarização de Dados");
            System.out.println("0 - Voltar");
            System.out.println("======================================");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine().trim());

                switch (opcao) {
                    case 1:
                        relatorioClientesContatos(matrizClientes, matrizContatos);
                        break;
                    case 2:
                        relatorioSumarizacao(matrizClientes, matrizContatos);
                        break;
                    case 0:
                        System.out.println("Voltando ao menu principal...");
                        break;
                    default:
                        System.out.println("Opção inválida!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida! Digite apenas números.");
                opcao = -1;
            }
        }
    }

    // =====================================================================
    // FUNÇÕES DE CARREGAMENTO DE DADOS (CSV)
    // =====================================================================

    /**
     * Carrega clientes do arquivo clientes.csv
     * Formato: codigo,nome,cpf_cnpj,data_nascimento,sexo,cidade,estado,status
     */
    private static String[][] carregarClientesCSV(String[][] matrizClientes) {
        String arquivo = "clientes.csv";
        int contador = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            boolean primeiraLinha = true;

            while ((linha = br.readLine()) != null) {
                if (primeiraLinha) { primeiraLinha = false; continue; }

                String[] dados = linha.split(",");
                if (dados.length >= 8) {
                    matrizClientes = aumentarMatrizClientes(matrizClientes);
                    int idx = matrizClientes.length - 1;
                    matrizClientes[idx][0] = dados[0].trim();
                    matrizClientes[idx][1] = dados[1].trim();
                    matrizClientes[idx][2] = dados[2].trim();
                    matrizClientes[idx][3] = dados[3].trim();
                    matrizClientes[idx][4] = dados[4].trim().toUpperCase();
                    matrizClientes[idx][5] = dados[5].trim();
                    matrizClientes[idx][6] = dados[6].trim().toUpperCase();
                    matrizClientes[idx][7] = dados[7].trim().toUpperCase();
                    contador++;
                }
            }
            System.out.println(contador + " cliente(s) carregado(s) do arquivo CSV.");

        } catch (IOException e) {
            System.out.println("Aviso: arquivo " + arquivo + " não encontrado. " + e.getMessage());
        }

        return matrizClientes;
    }

    /**
     * Carrega contatos do arquivo contatos.csv
     * Formato: codigo_contato,codigo_cliente,tipo,valor,status
     */
    private static String[][] carregarContatosCSV(String[][] matrizContatos) {
        String arquivo = "contatos.csv";
        int contador = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            boolean primeiraLinha = true;

            while ((linha = br.readLine()) != null) {
                if (primeiraLinha) { primeiraLinha = false; continue; }

                String[] dados = linha.split(",");
                if (dados.length >= 5) {
                    matrizContatos = aumentarMatrizContatos(matrizContatos);
                    int idx = matrizContatos.length - 1;
                    matrizContatos[idx][0] = dados[0].trim();
                    matrizContatos[idx][1] = dados[1].trim();
                    matrizContatos[idx][2] = dados[2].trim();
                    matrizContatos[idx][3] = dados[3].trim();
                    matrizContatos[idx][4] = dados[4].trim().toUpperCase();
                    contador++;
                }
            }
            System.out.println(contador + " contato(s) carregado(s) do arquivo CSV.");

        } catch (IOException e) {
            System.out.println("Aviso: arquivo " + arquivo + " não encontrado. " + e.getMessage());
        }

        return matrizContatos;
    }

    // =====================================================================
    // FUNÇÕES DE CRESCIMENTO MANUAL DE MATRIZES
    // =====================================================================

    /**
     * Aumenta a matriz de clientes em 1 linha.
     * Cria nova matriz com +1 linha e copia os dados existentes.
     */
    private static String[][] aumentarMatrizClientes(String[][] matrizClientes) {
        int linhasAtuais = matrizClientes.length;
        String[][] novaMatriz = new String[linhasAtuais + 1][8];

        for (int i = 0; i < linhasAtuais; i++) {
            copiarLinha(matrizClientes[i], novaMatriz[i], 8);
        }
        return novaMatriz;
    }

    /**
     * Aumenta a matriz de contatos em 1 linha.
     * Cria nova matriz com +1 linha e copia os dados existentes.
     */
    private static String[][] aumentarMatrizContatos(String[][] matrizContatos) {
        int linhasAtuais = matrizContatos.length;
        String[][] novaMatriz = new String[linhasAtuais + 1][5];

        for (int i = 0; i < linhasAtuais; i++) {
            copiarLinha(matrizContatos[i], novaMatriz[i], 5);
        }
        return novaMatriz;
    }

    // =====================================================================
    // FUNÇÕES AUXILIARES OBRIGATÓRIAS
    // =====================================================================

    /**
     * Copia os dados de uma linha de origem para uma linha de destino.
     * @param origem  linha de origem
     * @param destino linha de destino
     * @param colunas quantidade de colunas a copiar
     */
    private static void copiarLinha(String[] origem, String[] destino, int colunas) {
        for (int j = 0; j < colunas; j++) {
            destino[j] = origem[j];
        }
    }

    /**
     * Limpa todos os campos de uma linha (preenche com null).
     * @param linha   linha a ser limpa
     * @param colunas quantidade de colunas
     */
    private static void limparLinha(String[] linha, int colunas) {
        for (int j = 0; j < colunas; j++) {
            linha[j] = null;
        }
    }

    /**
     * Troca duas linhas de posição dentro de uma matriz.
     * @param matriz  matriz bidimensional
     * @param i       índice da primeira linha
     * @param j       índice da segunda linha
     */
    private static void trocarLinhas(String[][] matriz, int i, int j) {
        String[] temp = matriz[i];
        matriz[i] = matriz[j];
        matriz[j] = temp;
    }

    /**
     * Comparação manual de nomes caractere por caractere (ignora maiúsculas/minúsculas).
     * Retorna negativo se a < b, zero se a == b, positivo se a > b.
     */
    private static int compararNomeCharPorChar(String a, String b) {
        String al = paraMinusculo(a);
        String bl = paraMinusculo(b);
        int len = al.length() < bl.length() ? al.length() : bl.length();
        for (int i = 0; i < len; i++) {
            if (al.charAt(i) < bl.charAt(i)) return -1;
            if (al.charAt(i) > bl.charAt(i)) return  1;
        }
        if (al.length() < bl.length()) return -1;
        if (al.length() > bl.length()) return  1;
        return 0;
    }

    /**
     * Converte string para minúsculas sem usar toLowerCase().
     * Trata letras básicas A-Z e vogais acentuadas do português.
     */
    private static String paraMinusculo(String s) {
        if (s == null) return "";
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] >= 'A' && chars[i] <= 'Z') {
                chars[i] = (char)(chars[i] + 32);
            } else if (chars[i] == 'Á') chars[i] = 'á';
            else if (chars[i] == 'À') chars[i] = 'à';
            else if (chars[i] == 'Â') chars[i] = 'â';
            else if (chars[i] == 'Ã') chars[i] = 'ã';
            else if (chars[i] == 'É') chars[i] = 'é';
            else if (chars[i] == 'Ê') chars[i] = 'ê';
            else if (chars[i] == 'Í') chars[i] = 'í';
            else if (chars[i] == 'Ó') chars[i] = 'ó';
            else if (chars[i] == 'Ô') chars[i] = 'ô';
            else if (chars[i] == 'Õ') chars[i] = 'õ';
            else if (chars[i] == 'Ú') chars[i] = 'ú';
            else if (chars[i] == 'Ç') chars[i] = 'ç';
        }
        return new String(chars);
    }

    /** Gera próximo código disponível para clientes (maior código + 1) */
    private static int proximoCodigoCliente(String[][] matrizClientes) {
        int max = 0;
        for (int i = 0; i < matrizClientes.length; i++) {
            try {
                int cod = Integer.parseInt(matrizClientes[i][0]);
                if (cod > max) max = cod;
            } catch (NumberFormatException e) { /* ignora */ }
        }
        return max + 1;
    }

    /** Gera próximo código disponível para contatos (maior código + 1) */
    private static int proximoCodigoContato(String[][] matrizContatos) {
        int max = 0;
        for (int i = 0; i < matrizContatos.length; i++) {
            try {
                int cod = Integer.parseInt(matrizContatos[i][0]);
                if (cod > max) max = cod;
            } catch (NumberFormatException e) { /* ignora */ }
        }
        return max + 1;
    }

    /**
     * Busca índice do cliente pelo código.
     * @return índice na matriz, ou -1 se não encontrar
     */
    private static int buscarClientePorCodigo(String[][] matrizClientes, String codigo) {
        for (int i = 0; i < matrizClientes.length; i++) {
            if (matrizClientes[i][0] != null && matrizClientes[i][0].equals(codigo)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Busca índice do contato pelo código.
     * @return índice na matriz, ou -1 se não encontrar
     */
    private static int buscarContatoPorCodigo(String[][] matrizContatos, String codigo) {
        for (int i = 0; i < matrizContatos.length; i++) {
            if (matrizContatos[i][0] != null && matrizContatos[i][0].equals(codigo)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Busca nome do cliente pelo código (para JOIN manual na listagem de contatos).
     * @return nome do cliente, ou "(desconhecido)" se não encontrar
     */
    private static String buscarNomeCliente(String[][] matrizClientes, String codigoCliente) {
        for (int i = 0; i < matrizClientes.length; i++) {
            if (matrizClientes[i][0] != null && matrizClientes[i][0].equals(codigoCliente)) {
                return matrizClientes[i][1];
            }
        }
        return "(desconhecido)";
    }

    /**
     * Conta quantos contatos um cliente possui.
     */
    private static int contarContatosPorCliente(String[][] matrizContatos, String codigoCliente) {
        int total = 0;
        for (int i = 0; i < matrizContatos.length; i++) {
            if (matrizContatos[i][1] != null && matrizContatos[i][1].equals(codigoCliente)) {
                total++;
            }
        }
        return total;
    }

    // =====================================================================
    // FUNÇÕES DE CLIENTES
    // =====================================================================

    /**
     * Inclui novo cliente na matriz.
     * Código gerado automaticamente (maior existente + 1).
     */
    private static String[][] incluirCliente(Scanner scanner, String[][] matrizClientes) {
        System.out.println("\n--- INCLUIR CLIENTE ---");

        int novoCodigo = proximoCodigoCliente(matrizClientes);

        System.out.print("Nome        : ");
        String nome = scanner.nextLine().trim();
        System.out.print("CPF/CNPJ    : ");
        String cpf = scanner.nextLine().trim();
        System.out.print("Nascimento (DD/MM/AAAA): ");
        String nasc = scanner.nextLine().trim();
        System.out.print("Sexo (M/F)  : ");
        String sexo = scanner.nextLine().trim().toUpperCase();
        System.out.print("Cidade      : ");
        String cidade = scanner.nextLine().trim();
        System.out.print("Estado (UF) : ");
        String estado = scanner.nextLine().trim().toUpperCase();
        System.out.print("Status (ATIVO/INATIVO): ");
        String status = scanner.nextLine().trim().toUpperCase();

        matrizClientes = aumentarMatrizClientes(matrizClientes);
        int idx = matrizClientes.length - 1;

        matrizClientes[idx][0] = String.valueOf(novoCodigo);
        matrizClientes[idx][1] = nome;
        matrizClientes[idx][2] = cpf;
        matrizClientes[idx][3] = nasc;
        matrizClientes[idx][4] = sexo;
        matrizClientes[idx][5] = cidade;
        matrizClientes[idx][6] = estado;
        matrizClientes[idx][7] = status;

        System.out.println("Cliente incluído com código " + novoCodigo + "!");
        return matrizClientes;
    }

    /**
     * Lista todos os clientes em formato de tabela com separadores |
     */
    private static void listarClientesTabela(String[][] matrizClientes) {
        if (matrizClientes.length == 0) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }
        System.out.println("\n--- LISTA DE CLIENTES ---");
        System.out.printf("%-6s | %-20s | %-15s | %-10s | %-4s | %-18s | %-5s | %-8s%n",
                "Código", "Nome", "CPF/CNPJ", "Nascimento", "Sexo", "Cidade", "UF", "Status");
        System.out.println("-".repeat(100));
        for (int i = 0; i < matrizClientes.length; i++) {
            System.out.printf("%-6s | %-20s | %-15s | %-10s | %-4s | %-18s | %-5s | %-8s%n",
                    matrizClientes[i][0],
                    matrizClientes[i][1],
                    matrizClientes[i][2],
                    matrizClientes[i][3],
                    matrizClientes[i][4],
                    matrizClientes[i][5],
                    matrizClientes[i][6],
                    matrizClientes[i][7]);
        }
        System.out.println("-".repeat(100));
        System.out.println("Total: " + matrizClientes.length + " cliente(s).");
    }

    /**
     * Consulta cliente pelo código, exibindo também seus contatos vinculados.
     * Exibe a lista antes de pedir o código.
     */
    private static void consultarCliente(Scanner scanner,
                                          String[][] matrizClientes,
                                          String[][] matrizContatos) {
        System.out.println("\n--- CONSULTAR CLIENTE ---");
        listarClientesTabela(matrizClientes);

        System.out.print("\nInforme o código do cliente: ");
        String codigo = scanner.nextLine().trim();

        int idx = buscarClientePorCodigo(matrizClientes, codigo);
        if (idx == -1) {
            System.out.println("Cliente não encontrado.");
            return;
        }

        System.out.println("\nCliente encontrado:");
        System.out.printf("%-6s | %-20s | %-15s | %-10s | %-4s | %-18s | %-5s | %-8s%n",
                "Código", "Nome", "CPF/CNPJ", "Nascimento", "Sexo", "Cidade", "UF", "Status");
        System.out.println("-".repeat(100));
        System.out.printf("%-6s | %-20s | %-15s | %-10s | %-4s | %-18s | %-5s | %-8s%n",
                matrizClientes[idx][0], matrizClientes[idx][1],
                matrizClientes[idx][2], matrizClientes[idx][3],
                matrizClientes[idx][4], matrizClientes[idx][5],
                matrizClientes[idx][6], matrizClientes[idx][7]);

        System.out.println("\nContatos vinculados:");
        System.out.printf("%-8s | %-6s | %-15s | %-15s | %-30s | %-8s%n",
                "CodCont", "CodCli", "Nome Cliente", "Tipo", "Valor", "Status");
        System.out.println("-".repeat(95));

        boolean temContato = false;
        for (int i = 0; i < matrizContatos.length; i++) {
            if (matrizContatos[i][1] != null && matrizContatos[i][1].equals(codigo)) {
                System.out.printf("%-8s | %-6s | %-15s | %-15s | %-30s | %-8s%n",
                        matrizContatos[i][0], matrizContatos[i][1],
                        matrizClientes[idx][1],
                        matrizContatos[i][2], matrizContatos[i][3],
                        matrizContatos[i][4]);
                temContato = true;
            }
        }
        if (!temContato) System.out.println("  (nenhum contato cadastrado)");
        System.out.println("-".repeat(95));
    }

    /**
     * Altera dados de um cliente existente.
     * Exibe a lista antes de pedir o código.
     */
    private static String[][] alterarCliente(Scanner scanner, String[][] matrizClientes) {
        System.out.println("\n--- ALTERAR CLIENTE ---");
        listarClientesTabela(matrizClientes);

        System.out.print("\nQual cliente deseja alterar => ");
        String codigo = scanner.nextLine().trim();

        int idx = buscarClientePorCodigo(matrizClientes, codigo);
        if (idx == -1) {
            System.out.println("Cliente não encontrado.");
            return matrizClientes;
        }

        System.out.println("\nCliente encontrado:");
        System.out.printf("%-6s | %-20s | %-15s | %-10s | %-4s | %-18s | %-5s | %-8s%n",
                "Código", "Nome", "CPF/CNPJ", "Nascimento", "Sexo", "Cidade", "UF", "Status");
        System.out.println("-".repeat(100));
        System.out.printf("%-6s | %-20s | %-15s | %-10s | %-4s | %-18s | %-5s | %-8s%n",
                matrizClientes[idx][0], matrizClientes[idx][1],
                matrizClientes[idx][2], matrizClientes[idx][3],
                matrizClientes[idx][4], matrizClientes[idx][5],
                matrizClientes[idx][6], matrizClientes[idx][7]);

        System.out.print("\nDeseja alterar este cliente? (S/N): ");
        String conf = scanner.nextLine().trim().toUpperCase();
        if (!conf.equals("S")) {
            System.out.println("Operação cancelada.");
            return matrizClientes;
        }

        System.out.println("\nDigite os novos dados (Enter para manter o valor atual):");

        System.out.print("Nome [" + matrizClientes[idx][1] + "]: ");
        String nome = scanner.nextLine().trim();
        if (!nome.isEmpty()) matrizClientes[idx][1] = nome;

        System.out.print("CPF/CNPJ [" + matrizClientes[idx][2] + "]: ");
        String cpf = scanner.nextLine().trim();
        if (!cpf.isEmpty()) matrizClientes[idx][2] = cpf;

        System.out.print("Nascimento [" + matrizClientes[idx][3] + "]: ");
        String nasc = scanner.nextLine().trim();
        if (!nasc.isEmpty()) matrizClientes[idx][3] = nasc;

        System.out.print("Sexo [" + matrizClientes[idx][4] + "]: ");
        String sexo = scanner.nextLine().trim().toUpperCase();
        if (!sexo.isEmpty()) matrizClientes[idx][4] = sexo;

        System.out.print("Cidade [" + matrizClientes[idx][5] + "]: ");
        String cidade = scanner.nextLine().trim();
        if (!cidade.isEmpty()) matrizClientes[idx][5] = cidade;

        System.out.print("Estado [" + matrizClientes[idx][6] + "]: ");
        String estado = scanner.nextLine().trim().toUpperCase();
        if (!estado.isEmpty()) matrizClientes[idx][6] = estado;

        System.out.print("Status [" + matrizClientes[idx][7] + "]: ");
        String status = scanner.nextLine().trim().toUpperCase();
        if (!status.isEmpty()) matrizClientes[idx][7] = status;

        System.out.println("Cliente alterado com sucesso!");
        return matrizClientes;
    }

    /**
     * Apaga cliente e todos os seus contatos vinculados.
     * Exibe a lista antes de pedir o código.
     * Retorna Object[]{matrizClientes, matrizContatos} atualizadas.
     */
    private static Object[] apagarCliente(Scanner scanner,
                                           String[][] matrizClientes,
                                           String[][] matrizContatos) {
        System.out.println("\n--- APAGAR CLIENTE ---");
        listarClientesTabela(matrizClientes);

        System.out.print("\nQual cliente deseja apagar => ");
        String codigo = scanner.nextLine().trim();

        int idx = buscarClientePorCodigo(matrizClientes, codigo);
        if (idx == -1) {
            System.out.println("Cliente não encontrado.");
            return new Object[]{matrizClientes, matrizContatos};
        }

        System.out.println("\nCliente encontrado:");
        System.out.printf("%-6s | %-20s | %-15s | %-10s | %-4s | %-18s | %-5s | %-8s%n",
                "Código", "Nome", "CPF/CNPJ", "Nascimento", "Sexo", "Cidade", "UF", "Status");
        System.out.println("-".repeat(100));
        System.out.printf("%-6s | %-20s | %-15s | %-10s | %-4s | %-18s | %-5s | %-8s%n",
                matrizClientes[idx][0], matrizClientes[idx][1],
                matrizClientes[idx][2], matrizClientes[idx][3],
                matrizClientes[idx][4], matrizClientes[idx][5],
                matrizClientes[idx][6], matrizClientes[idx][7]);

        System.out.print("\nConfirma exclusão? (S/N): ");
        String conf = scanner.nextLine().trim().toUpperCase();

        if (!conf.equals("S")) {
            System.out.println("Operação cancelada.");
            return new Object[]{matrizClientes, matrizContatos};
        }

        // 1. Remove contatos vinculados ao cliente
        int contatosRemovidos = 0;
        for (int i = 0; i < matrizContatos.length; i++) {
            if (matrizContatos[i][1] != null && matrizContatos[i][1].equals(codigo)) {
                matrizContatos = removerLinhaContatos(matrizContatos, i);
                i--; // ajusta índice após remoção
                contatosRemovidos++;
            }
        }

        // 2. Remove o cliente
        matrizClientes = removerLinhaClientes(matrizClientes, idx);

        System.out.println("Cliente removido com sucesso! Contatos removidos: " + contatosRemovidos);
        return new Object[]{matrizClientes, matrizContatos};
    }

    /**
     * Ordena a matriz de clientes por nome usando Bubble Sort manual.
     * Usa compararNomeCharPorChar e trocarLinhas conforme funções obrigatórias.
     */
    private static String[][] ordenarClientesPorNome(String[][] matrizClientes) {
        int n = matrizClientes.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                if (compararNomeCharPorChar(matrizClientes[j][1], matrizClientes[j + 1][1]) > 0) {
                    trocarLinhas(matrizClientes, j, j + 1);
                }
            }
        }
        return matrizClientes;
    }

    // =====================================================================
    // FUNÇÕES DE CONTATOS
    // =====================================================================

    /**
     * Inclui novo contato vinculado a um cliente.
     * Valida se o cliente existe antes de incluir.
     */
    private static String[][] incluirContato(Scanner scanner,
                                              String[][] matrizClientes,
                                              String[][] matrizContatos) {
        System.out.println("\n--- INCLUIR CONTATO ---");
        System.out.print("Digite o código do cliente: ");
        String codigoCliente = scanner.nextLine().trim();

        int idxCliente = buscarClientePorCodigo(matrizClientes, codigoCliente);
        if (idxCliente == -1) {
            System.out.println("Cliente não encontrado. O contato só pode ser incluído se o cliente existir.");
            return matrizContatos;
        }

        System.out.println("Cliente encontrado: " + matrizClientes[idxCliente][1]);

        int novoCod = proximoCodigoContato(matrizContatos);

        System.out.println("Tipos sugeridos: Telefone | WhatsApp | Email | Instagram | LinkedIn | Site | Outro");
        System.out.print("Digite o tipo do contato: ");
        String tipo = scanner.nextLine().trim();
        System.out.print("Digite o valor do contato: ");
        String valor = scanner.nextLine().trim();

        matrizContatos = aumentarMatrizContatos(matrizContatos);
        int idx = matrizContatos.length - 1;
        matrizContatos[idx][0] = String.valueOf(novoCod);
        matrizContatos[idx][1] = codigoCliente;
        matrizContatos[idx][2] = tipo;
        matrizContatos[idx][3] = valor;
        matrizContatos[idx][4] = "ATIVO";

        System.out.println("Contato incluído com código " + novoCod + "!");
        return matrizContatos;
    }

    /**
     * Lista TODOS os contatos de todos os clientes em formato de tabela.
     * Faz JOIN manual com a matriz de clientes para exibir o nome.
     */
    private static void listarContatosTabela(String[][] matrizClientes,
                                              String[][] matrizContatos) {
        if (matrizContatos.length == 0) {
            System.out.println("Nenhum contato cadastrado.");
            return;
        }
        System.out.println("\n--- LISTA DE CONTATOS (TODOS OS CLIENTES) ---");
        System.out.printf("%-8s | %-6s | %-20s | %-12s | %-30s | %-8s%n",
                "CodCont", "CodCli", "Nome Cliente", "Tipo", "Valor", "Status");
        System.out.println("-".repeat(98));
        for (int i = 0; i < matrizContatos.length; i++) {
            String nomeCliente = buscarNomeCliente(matrizClientes, matrizContatos[i][1]);
            System.out.printf("%-8s | %-6s | %-20s | %-12s | %-30s | %-8s%n",
                    matrizContatos[i][0],
                    matrizContatos[i][1],
                    nomeCliente,
                    matrizContatos[i][2],
                    matrizContatos[i][3],
                    matrizContatos[i][4]);
        }
        System.out.println("-".repeat(98));
        System.out.println("Total: " + matrizContatos.length + " contato(s).");
    }

    /**
     * Lista apenas os contatos de um cliente específico.
     * Exibe a lista geral primeiro, depois filtra pelo cliente selecionado.
     */
    private static void listarContatosPorCliente(Scanner scanner,
                                                  String[][] matrizClientes,
                                                  String[][] matrizContatos) {
        System.out.println("\n--- LISTAR CONTATOS DE UM CLIENTE ---");

        // Exibe todos os contatos primeiro
        listarContatosTabela(matrizClientes, matrizContatos);

        System.out.print("\nSelecione o cliente que quer listar os contatos => ");
        String codigoCliente = scanner.nextLine().trim();

        int idxCliente = buscarClientePorCodigo(matrizClientes, codigoCliente);
        if (idxCliente == -1) {
            System.out.println("Cliente não encontrado.");
            return;
        }

        System.out.println("\nContatos de " + matrizClientes[idxCliente][1] + ":");
        System.out.printf("%-8s | %-6s | %-20s | %-12s | %-30s | %-8s%n",
                "CodCont", "CodCli", "Nome Cliente", "Tipo", "Valor", "Status");
        System.out.println("-".repeat(98));

        boolean temContato = false;
        for (int i = 0; i < matrizContatos.length; i++) {
            if (matrizContatos[i][1] != null && matrizContatos[i][1].equals(codigoCliente)) {
                System.out.printf("%-8s | %-6s | %-20s | %-12s | %-30s | %-8s%n",
                        matrizContatos[i][0],
                        matrizContatos[i][1],
                        matrizClientes[idxCliente][1],
                        matrizContatos[i][2],
                        matrizContatos[i][3],
                        matrizContatos[i][4]);
                temContato = true;
            }
        }
        if (!temContato) System.out.println("  Nenhum contato cadastrado para este cliente.");
        System.out.println("-".repeat(98));
    }

    /**
     * Altera dados de um contato.
     * Exibe a lista de todos os contatos antes de pedir o código.
     */
    private static String[][] alterarContato(Scanner scanner,
                                              String[][] matrizClientes,
                                              String[][] matrizContatos) {
        System.out.println("\n--- ALTERAR CONTATO ---");
        listarContatosTabela(matrizClientes, matrizContatos);

        System.out.print("\nQual contato deseja alterar => ");
        String codContato = scanner.nextLine().trim();

        int idx = buscarContatoPorCodigo(matrizContatos, codContato);
        if (idx == -1) {
            System.out.println("Contato não encontrado.");
            return matrizContatos;
        }

        System.out.println("\nContato encontrado:");
        System.out.printf("%-8s | %-6s | %-12s | %-30s | %-8s%n",
                "CodCont", "CodCli", "Tipo", "Valor", "Status");
        System.out.println("-".repeat(72));
        System.out.printf("%-8s | %-6s | %-12s | %-30s | %-8s%n",
                matrizContatos[idx][0], matrizContatos[idx][1],
                matrizContatos[idx][2], matrizContatos[idx][3],
                matrizContatos[idx][4]);

        System.out.print("\nDeseja alterar este contato? (S/N): ");
        String conf = scanner.nextLine().trim().toUpperCase();
        if (!conf.equals("S")) {
            System.out.println("Operação cancelada.");
            return matrizContatos;
        }

        System.out.println("\nDigite os novos dados (Enter para manter o valor atual):");

        System.out.print("Tipo [" + matrizContatos[idx][2] + "]: ");
        String tipo = scanner.nextLine().trim();
        if (!tipo.isEmpty()) matrizContatos[idx][2] = tipo;

        System.out.print("Valor [" + matrizContatos[idx][3] + "]: ");
        String valor = scanner.nextLine().trim();
        if (!valor.isEmpty()) matrizContatos[idx][3] = valor;

        // Status sempre ATIVO conforme regra do PDF
        matrizContatos[idx][4] = "ATIVO";

        System.out.println("Contato alterado com sucesso!");
        return matrizContatos;
    }

    /**
     * Apaga um contato pelo código.
     * Exibe a lista de todos os contatos antes de pedir o código.
     */
    private static String[][] apagarContato(Scanner scanner,
                                             String[][] matrizClientes,
                                             String[][] matrizContatos) {
        System.out.println("\n--- APAGAR CONTATO ---");
        listarContatosTabela(matrizClientes, matrizContatos);

        System.out.print("\nQual contato deseja apagar => ");
        String codigo = scanner.nextLine().trim();

        int idx = buscarContatoPorCodigo(matrizContatos, codigo);
        if (idx == -1) {
            System.out.println("Contato não encontrado.");
            return matrizContatos;
        }

        System.out.println("\nContato encontrado:");
        System.out.printf("%-8s | %-6s | %-12s | %-30s | %-8s%n",
                "CodCont", "CodCli", "Tipo", "Valor", "Status");
        System.out.println("-".repeat(72));
        System.out.printf("%-8s | %-6s | %-12s | %-30s | %-8s%n",
                matrizContatos[idx][0], matrizContatos[idx][1],
                matrizContatos[idx][2], matrizContatos[idx][3],
                matrizContatos[idx][4]);

        System.out.print("\nConfirma exclusão? (S/N): ");
        String conf = scanner.nextLine().trim().toUpperCase();

        if (!conf.equals("S")) {
            System.out.println("Operação cancelada.");
            return matrizContatos;
        }

        matrizContatos = removerLinhaContatos(matrizContatos, idx);
        System.out.println("Contato removido com sucesso!");
        return matrizContatos;
    }

    // =====================================================================
    // FUNÇÕES DE RELATÓRIOS
    // =====================================================================

    /**
     * Relatório: Lista clientes com total de contatos por cliente.
     * Exibe totais gerais ao final.
     */
    private static void relatorioClientesContatos(String[][] matrizClientes,
                                                   String[][] matrizContatos) {
        if (matrizClientes.length == 0) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }

        System.out.println("\n========== RELATÓRIO: CLIENTES E CONTATOS ==========");
        System.out.printf("%-6s | %-20s | %-5s | %-8s | %-15s%n",
                "Código", "Nome", "UF", "Status", "Qtd Contatos");
        System.out.println("-".repeat(65));

        for (int i = 0; i < matrizClientes.length; i++) {
            int qtdContatos = contarContatosPorCliente(matrizContatos, matrizClientes[i][0]);
            System.out.printf("%-6s | %-20s | %-5s | %-8s | %-15d%n",
                    matrizClientes[i][0],
                    matrizClientes[i][1],
                    matrizClientes[i][6],
                    matrizClientes[i][7],
                    qtdContatos);
        }

        System.out.println("-".repeat(65));
        System.out.println("Total de clientes : " + matrizClientes.length);
        System.out.println("Total de contatos : " + matrizContatos.length);
        System.out.println("=====================================================");
    }

    /**
     * Relatório: Sumarização de dados.
     * Total de clientes, total de contatos, média de contatos/cliente, clientes sem contato.
     */
    private static void relatorioSumarizacao(String[][] matrizClientes,
                                              String[][] matrizContatos) {
        System.out.println("\n========== RELATÓRIO: SUMARIZAÇÃO ==========");

        int totalClientes = matrizClientes.length;
        int totalContatos = matrizContatos.length;

        // Calcula clientes sem contato
        int clientesSemContato = 0;
        for (int i = 0; i < matrizClientes.length; i++) {
            if (contarContatosPorCliente(matrizContatos, matrizClientes[i][0]) == 0) {
                clientesSemContato++;
            }
        }

        // Média de contatos por cliente (inteiro para não usar double avançado)
        int mediaInteira = 0;
        int mediaDecimal = 0;
        if (totalClientes > 0) {
            mediaInteira  = totalContatos / totalClientes;
            mediaDecimal  = (totalContatos * 10 / totalClientes) % 10;
        }

        System.out.println("Total de clientes          : " + totalClientes);
        System.out.println("Total de contatos          : " + totalContatos);
        System.out.println("Média de contatos/cliente  : " + mediaInteira + "," + mediaDecimal);
        System.out.println("Clientes sem contato       : " + clientesSemContato);

        // Lista os clientes sem contato (se houver)
        if (clientesSemContato > 0) {
            System.out.println("\nClientes sem contato cadastrado:");
            for (int i = 0; i < matrizClientes.length; i++) {
                if (contarContatosPorCliente(matrizContatos, matrizClientes[i][0]) == 0) {
                    System.out.println("  [" + matrizClientes[i][0] + "] " + matrizClientes[i][1]);
                }
            }
        }
        System.out.println("=============================================");
    }

    // =====================================================================
    // FUNÇÕES DE REMOÇÃO DE LINHAS DAS MATRIZES
    // =====================================================================

    /**
     * Remove uma linha da matriz de clientes criando nova matriz com -1 linha.
     * Usa copiarLinha para copiar os dados.
     */
    private static String[][] removerLinhaClientes(String[][] matrizClientes, int indiceRemover) {
        int novoTamanho = matrizClientes.length - 1;
        String[][] novaMatriz = new String[novoTamanho][8];
        int novoIdx = 0;
        for (int i = 0; i < matrizClientes.length; i++) {
            if (i != indiceRemover) {
                copiarLinha(matrizClientes[i], novaMatriz[novoIdx], 8);
                novoIdx++;
            }
        }
        return novaMatriz;
    }

    /**
     * Remove uma linha da matriz de contatos criando nova matriz com -1 linha.
     * Usa copiarLinha para copiar os dados.
     */
    private static String[][] removerLinhaContatos(String[][] matrizContatos, int indiceRemover) {
        int novoTamanho = matrizContatos.length - 1;
        String[][] novaMatriz = new String[novoTamanho][5];
        int novoIdx = 0;
        for (int i = 0; i < matrizContatos.length; i++) {
            if (i != indiceRemover) {
                copiarLinha(matrizContatos[i], novaMatriz[novoIdx], 5);
                novoIdx++;
            }
        }
        return novaMatriz;
    }
}
