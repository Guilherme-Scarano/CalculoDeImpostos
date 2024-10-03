package calculaprecofinal;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Produto {
    String nome;
    double valor;
    double frete;
    double seguro;
    double margemLucro;
    String ufOrigem;
    String ufDestino;

    public Produto(String nome, double valor, double frete, double seguro, double margemLucro) {
        this.nome = nome;
        this.valor = valor;
        this.frete = frete;
        this.seguro = seguro;
        this.margemLucro = margemLucro;
    }

    public void setUfs(String ufOrigem, String ufDestino) {
        this.ufOrigem = ufOrigem;
        this.ufDestino = ufDestino;
    }

    public double calcularIPI() {
        double baseCalculo = valor + frete + seguro;
        return baseCalculo * 0.0015;
    }

    public double calcularICMS() {
        double aliquota = 0;
        if (ufOrigem.equals("SP") && ufDestino.equals("RJ")) {
            aliquota = 0.12;
        } else if (ufOrigem.equals("SP") && ufDestino.equals("DF")) {
            aliquota = 0.07;
        } else {
            System.out.println("UF de origem e destino não cadastradas para cálculo de ICMS.");
            return -1;
        }
        return valor * aliquota;
    }

    public double calcularPrecoFinal() {
        double ipi = calcularIPI();
        double icms = calcularICMS();
        if (icms == -1) {
            return -1; 
        }
        return (valor + ipi + icms) * (1 + margemLucro);
    }

    @Override
    public String toString() {
        double precoFinal = calcularPrecoFinal();
        if (precoFinal == -1) {
            return "Não foi possível calcular o preço final devido à combinação de UFs inválida.";
        }
        return "Produto: " + nome + "\nValor: R$ " + valor + "\nFrete: R$ " + frete + "\nSeguro: R$ " + seguro + "\nIPI: R$ " + calcularIPI() + 
               "\nICMS: R$ " + calcularICMS() + "\nPreço Final (com margem de lucro): R$ " + precoFinal;
    }
}

class Servico {
    String nome;
    double valor;
    double aliquotaISS;
    double margemLucro;

    public Servico(String nome, double valor, double aliquotaISS, double margemLucro) {
        this.nome = nome;
        this.valor = valor;
        this.aliquotaISS = aliquotaISS;
        this.margemLucro = margemLucro;
    }

    public double calcularISS() {
        return valor * aliquotaISS;
    }

    public double calcularPrecoFinal() {
        double iss = calcularISS();
        return (valor + iss) * (1 + margemLucro);
    }

    @Override
    public String toString() {
        return "Serviço: " + nome + "\nValor: R$ " + valor + "\nISS: R$ " + calcularISS() + 
               "\nPreço Final (com margem de lucro): R$ " + calcularPrecoFinal();
    }
}

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<Produto> produtos = new ArrayList<>();
        produtos.add(new Produto("Notebook", 4000, 100, 50, 0.20));
        produtos.add(new Produto("Smartphone", 2500, 80, 40, 0.15));
        produtos.add(new Produto("Tablet", 1500, 50, 30, 0.10));

        List<Servico> servicos = new ArrayList<>();
        servicos.add(new Servico("Desenvolvimento de Software", 5000, 0.05, 0.30));
        servicos.add(new Servico("Consultoria", 3000, 0.03, 0.25));
        servicos.add(new Servico("Design Gráfico", 2000, 0.04, 0.20));

        System.out.println("Produtos disponíveis:\n");
        for (int i = 0; i < produtos.size(); i++) {
            System.out.println(i + 1 + ". " + produtos.get(i).nome + " - R$ " + produtos.get(i).valor);
        }

        System.out.println("\nServiços disponíveis:\n");
        for (int i = 0; i < servicos.size(); i++) {
            System.out.println(i + 1 + ". " + servicos.get(i).nome + " - R$ " + servicos.get(i).valor);
        }

        System.out.println("\nDigite 1 para Produto ou 2 para Serviço:");
        int escolha = scanner.nextInt();

        if (escolha == 1) {
            System.out.println("Escolha o número do produto:");
            int produtoEscolhido = scanner.nextInt() - 1;

            System.out.println("Digite a UF de origem (Só enviamos de SP):");
            String ufOrigem = scanner.next().toUpperCase();
            System.out.println("Digite a UF de destino (Atualmente nossas rotas dísponiveis são RJ e DF):");
            String ufDestino = scanner.next().toUpperCase();

            Produto produto = produtos.get(produtoEscolhido);
            produto.setUfs(ufOrigem, ufDestino);

            System.out.println("\nCálculo do preço final para o produto selecionado:\n");
            System.out.println(produto);

        } else if (escolha == 2) {
            System.out.println("Escolha o número do serviço:");
            int servicoEscolhido = scanner.nextInt() - 1;

            Servico servico = servicos.get(servicoEscolhido);
            System.out.println("\nCálculo do preço final para o serviço selecionado:\n");
            System.out.println(servico);
        } else {
            System.out.println("Opção inválida.");
        }

        scanner.close();
    }
}