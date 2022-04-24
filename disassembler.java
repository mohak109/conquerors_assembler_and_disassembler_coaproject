import java.util.*;

class mcdisassembler {

    mipsreg converter = new mipsreg();

    String jtype(String mc) {
        String ac = "";

        String[][] func = { { "j", "000010" },
                { "jal", "000011" } };

        for (int i = 0; i < func.length; i++) {
            if (mc.substring(0, 6).equalsIgnoreCase(func[i][1])) {
                ac += func[i][0] + " ";
            }
        }

        ac += Integer.parseInt(mc.substring(6), 2) / 4;

        return ac;
    }

    String itype(String mc) {
        String ac = "";

        String[][] func = { { "lw", "100011" },
                { "sw", "101011" },
                { "lb", "100000" },
                { "sb", "101000" },
                { "beq", "000100" },
                { "bne", "000101" },
                { "addi", "001000" },
                { "andi", "001100" } };

        for (int i = 0; i < func.length; i++) {
            if (mc.substring(0, 6).equalsIgnoreCase(func[i][1])) {
                ac += func[i][0] + " ";
            }
        }

        if (ac.charAt(0) == 'l' || ac.charAt(0) == 's') {
            if (ac.charAt(0) == 's') {
                ac += converter.convtoreg(Integer.parseInt(mc.substring(6, 11), 2)) + " "
                        + Integer.parseInt(mc.substring(16), 2) + "("
                        + converter.convtoreg(Integer.parseInt(mc.substring(11, 16), 2)) + ")";
            } else {
                ac += converter.convtoreg(Integer.parseInt(mc.substring(11, 16), 2)) + " "
                        + Integer.parseInt(mc.substring(16), 2) + "("
                        + converter.convtoreg(Integer.parseInt(mc.substring(6, 11), 2)) + ")";
            }
        } else {
            if (ac.charAt(0) == 'b') {
                ac += converter.convtoreg(Integer.parseInt(mc.substring(6, 11), 2)) + " "
                        + converter.convtoreg(Integer.parseInt(mc.substring(11, 16), 2)) + " "
                        + (Integer.parseInt(mc.substring(16), 2) / 4);
            } else {
                ac += converter.convtoreg(Integer.parseInt(mc.substring(11, 16), 2)) + " "
                        + converter.convtoreg(Integer.parseInt(mc.substring(6, 11), 2)) + " "
                        + (Integer.parseInt(mc.substring(16), 2));
            }
        }

        return ac;
    }

    String rtype(String mc) {
        String ac = "";

        String[][] func = { { "add", "100000" },
                { "sub", "100010" },
                { "and", "100100" },
                { "or", "100101" },
                { "nor", "100111" },
                { "slt", "101010" },
                { "sltu", "101011" },
                { "sll", "000000" },
                { "srl", "000010" },
                { "jr", "001000" } };

        // int[] mcDeci = new int[mc.length];

        // for (int i = 0; i < mc.length; i++) {
        // mcDeci[i] = Integer.parseInt(mc[i], 2);
        // }

        for (int i = 0; i < func.length; i++) {
            if (mc.substring(26).equalsIgnoreCase(func[i][1])) {
                ac += func[i][0] + " ";
            }
        }

        if (ac.equalsIgnoreCase("jr ")) {
            ac += converter.convtoreg(Integer.parseInt(mc.substring(6, 11), 2));
        } else if (ac.equalsIgnoreCase("sll ") || ac.equalsIgnoreCase("srl ")) {
            ac += converter.convtoreg(Integer.parseInt(mc.substring(16, 21), 2)) + " "
                    + converter.convtoreg(Integer.parseInt(mc.substring(6, 11), 2)) + " "
                    + (Integer.parseInt(mc.substring(21, 26), 2));
        } else {
            ac += converter.convtoreg(Integer.parseInt(mc.substring(16, 21), 2)) + " "
                    + converter.convtoreg(Integer.parseInt(mc.substring(6, 11), 2)) + " "
                    + converter.convtoreg(Integer.parseInt(mc.substring(11, 16), 2));
        }

        return ac;
    }

}

public class disassembler {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("------------------------------");
        System.out.println("|||||||| DISASSEMBLER ||||||||");
        System.out.println("------------------------------");
        System.out.println("Enter your machine code you want to disassemble:");
        System.out.println("Press 0 when you finish your code");
        String mc = scan.nextLine();
        mipsreg converter = new mipsreg();
        mcdisassembler mcdis = new mcdisassembler();
        String ac = "";

        try{
            while (!mc.equalsIgnoreCase("0")) {
                mc = converter.hextobin(mc.substring(2));
                if (Integer.parseInt(mc.substring(0, 6), 2) == 0) {
                    // rtype
                    ac = mcdis.rtype(mc);
                } else if (Integer.parseInt(mc.substring(0, 6), 2) == 2 || Integer.parseInt(mc.substring(0, 6), 2) == 3) {
                    // jtype
                    ac = mcdis.jtype(mc);
                } else if (Integer.parseInt(mc.substring(0, 6), 2) > 3 /*&& Integer.parseInt(mc.substring(0, 6), 2) < 44*/) {
                    // itype
                    ac = mcdis.itype(mc);
                } else {
                    System.out.println("Check your code, there must be an error!!");
                }
                System.out.println(ac);
                mc = scan.nextLine();
            }
        } catch (Exception e) {
            System.out.println("Check your code, there must be an error!!");
        }

        scan.close();
    }

}

class mipsreg {
    String inttobin(int n) {
        String bin = "";

        while (n > 1) {
            bin = n % 2 + bin;
            n = n / 2;
        }
        bin = n + bin;
        if (bin.length() < 5) {
            while (bin.length() < 5) {
                bin = "0" + bin;
            }
        }

        return bin;
    }

    int regtoconv(String reg) {
        int conv = 0;

        if (reg.charAt(0) != '$') {
            conv = 0;
        } else if (reg.charAt(1) == 'a') {
            if (reg.charAt(2) == 't') {
                conv = 1;
            } else {
                conv = 4 + Integer.parseInt(reg.substring(2, 3));
            }
        } else if (reg.charAt(1) == 'v') {
            conv = 2 + Integer.parseInt(reg.substring(2, 3));
        } else if (reg.charAt(1) == 't') {
            conv = 8 + Integer.parseInt(reg.substring(2, 3));
            if (Integer.parseInt(reg.substring(2, 3)) > 7) {
                conv += 8;
            }
        } else if (reg.charAt(1) == 'k') {
            conv = 26 + Integer.parseInt(reg.substring(2, 3));
        } else if (reg.charAt(1) == 's') {
            conv = 16 + Integer.parseInt(reg.substring(2, 3));
        } else if (reg.charAt(2) == 'p') {
            if (reg.charAt(1) == 'g') {
                conv = 28;
            } else if (reg.charAt(1) == 's') {
                conv = 29;
            } else if (reg.charAt(1) == 'f') {
                conv = 30;
            }
        } else if (reg.charAt(1) == 'r') {
            conv = 31;
        }

        return conv;
    }

    String convtoreg(int n) {
        String reg = "";

        if (n == 0) {
            reg = "$zero";
        } else if (n == 1) {
            reg = "$at";
        } else if (n >= 2 && n < 4) {
            reg = "$v" + (n - 2);
        } else if (n >= 4 && n < 8) {
            reg = "$a" + (n - 4);
        } else if (n >= 8 && n < 16) {
            reg = "$t" + (n - 8);
        } else if (n >= 16 && n < 24) {
            reg = "$s" + (n - 16);
        } else if (n >= 24 && n < 26) {
            reg = "$t" + (n - 16);
        } else if (n >= 26 && n < 28) {
            reg = "$k" + (n - 26);
        } else if (n == 28) {
            reg = "$gp";
        } else if (n == 29) {
            reg = "$sp";
        } else if (n == 30) {
            reg = "$fp";
        } else if (n == 31) {
            reg = "$ra";
        }

        return reg;
    }

    String bintohex(String bin) {
        long dec = Long.parseLong(bin, 2);
        String hex = Long.toHexString(dec);
        return hex;
    }

    String hextobin(String hex) {
        long dec = Long.parseLong(hex, 16);
        String bin = Long.toBinaryString(dec);
        while (bin.length() < 32) {
            bin = "0" + bin;
        }
        return bin;
    }
}
