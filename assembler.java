import java.util.*;

class mcassembler {
    mipsreg converter = new mipsreg();

    String[] rtype(String ins, String des, String src1, String src2) {
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

        String funcCode = "";
        for (int i = 0; i < func.length; i++) {
            if (ins.equalsIgnoreCase(func[i][0])) {
                funcCode = func[i][1];
            }
        }

        String[] arr = new String[7];

        arr[0] = "000000";
        arr[1] = converter.inttobin(converter.regtoconv(src1));
        arr[2] = converter.inttobin(converter.regtoconv(src2));
        arr[3] = converter.inttobin(converter.regtoconv(des));
        // arr[4]
        if (ins.equalsIgnoreCase("sll") || ins.equalsIgnoreCase("srl")) {
            arr[4] = converter.inttobin(Integer.parseInt(src2));
        } else {
            arr[4] = "00000";
        }
        arr[arr.length - 2] = funcCode;

        String bin = "";
        for (int i = 0; i < arr.length; i++) {
            bin += arr[i];
        }
        String hex = "";
        for (int i = 32; i > 0; i -= 4) {
            hex = converter.bintohex(bin.substring(i - 4, i)) + hex;
        }
        arr[arr.length - 1] = hex;

        return arr;
    }

    String[] itype(String ins, String des, String src, String cons) {
        String[][] func = { { "lw", "100011" },
                { "sw", "101011" },
                { "lb", "100000" },
                { "sb", "101000" },
                { "beq", "000100" },
                { "bne", "000101" },
                { "addi", "001000" },
                { "andi", "001100" } };

        String op = "";
        for (int i = 0; i < func.length; i++) {
            if (ins.equalsIgnoreCase(func[i][0])) {
                op = func[i][1];
            }
        }

        String[] arr = new String[5];
        if (ins.charAt(0) == 's' || ins.charAt(0) == 'b') {
            arr[0] = op;
            arr[1] = converter.inttobin(converter.regtoconv(des));
            arr[2] = converter.inttobin(converter.regtoconv(src));
        } else {
            arr[0] = op;
            arr[1] = converter.inttobin(converter.regtoconv(src));
            arr[2] = converter.inttobin(converter.regtoconv(des));
        }
        if (ins.charAt(0) == 'b') {
            cons = converter.inttobin(Integer.parseInt(cons) * 4);
        } else {
            cons = converter.inttobin(Integer.parseInt(cons));
        }

        if (cons.length() < 16) {
            while (cons.length() < 16) {
                cons = "0" + cons;
            }
        }
        arr[3] = cons;

        String bin = "";
        for (int i = 0; i < arr.length; i++) {
            bin += arr[i];
        }
        String hex = "";
        for (int i = 32; i > 0; i -= 4) {
            hex = converter.bintohex(bin.substring(i - 4, i)) + hex;
        }
        arr[arr.length - 1] = hex;

        return arr;
    }

    String[] jtype(String ins, String address) {
        String[][] func = { { "j", "000010" },
                { "jal", "000011" } };

        String op = "";
        for (int i = 0; i < func.length; i++) {
            if (ins.equalsIgnoreCase(func[i][0])) {
                op = func[i][1];
            }
        }

        String[] arr = new String[3];

        arr[0] = op;
        address = converter.inttobin(Integer.parseInt(address) * 4);
        if (address.length() < 26) {
            while (address.length() < 26) {
                address = "0" + address;
            }
        }
        arr[1] = address;

        String bin = "";
        for (int i = 0; i < arr.length; i++) {
            bin += arr[i];
        }
        String hex = "";
        for (int i = 32; i > 0; i -= 4) {
            hex = converter.bintohex(bin.substring(i - 4, i)) + hex;
        }
        arr[arr.length - 1] = hex;

        return arr;
    }
}

public class assembler {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("---------------------------");
        System.out.println("|||||||| ASSEMBLER ||||||||");
        System.out.println("---------------------------");
        System.out.println("Enter the assembly code you want to assemble:");
        System.out.println("Press 0 when you finish your code");

        mcassembler mcass = new mcassembler();
        String[] line = scan.nextLine().split(" ");
        String ins = line[0];

        try{
            while (ins.charAt(0) != '0') {
                if (ins.equalsIgnoreCase("add") || ins.equalsIgnoreCase("sub") || ins.equalsIgnoreCase("and")
                        || ins.equalsIgnoreCase("or") || ins.equalsIgnoreCase("nor") || ins.equalsIgnoreCase("slt")
                        || ins.equalsIgnoreCase("sltu") || ins.equalsIgnoreCase("sll") || ins.equalsIgnoreCase("srl")
                        || ins.equalsIgnoreCase("jr")) {
                    // r type beginning
                    String[] mcr = new String[6];

                    if (ins.equalsIgnoreCase("jr")) {
                        String des = line[1];
                        mcr = mcass.rtype(ins, "0", des, "0");
                    } else {
                        String des = line[1];
                        String src1 = line[2];
                        String src2 = line[3];

                        mcr = mcass.rtype(ins, des, src1, src2);
                    }

                    for (int i = 0; i < mcr.length - 1; i++) {
                        System.out.print(mcr[i] + " ");
                    }
                    System.out.println();
                    System.out.print("0x" + mcr[mcr.length - 1]);
                    // rtype ending
                } else if (ins.equalsIgnoreCase("lw") || ins.equalsIgnoreCase("sw") || ins.equalsIgnoreCase("lb")
                        || ins.equalsIgnoreCase("sb") || ins.equalsIgnoreCase("beq") || ins.equalsIgnoreCase("bne")
                        || ins.equalsIgnoreCase("addi") || ins.equalsIgnoreCase("andi")) {
                    // itype beginning
                    String[] mci = new String[4];

                    if (ins.charAt(0) == 'l' || ins.charAt(0) == 's') {
                        String des = line[1];
                        String str = line[2];

                        String cons = "";
                        boolean chk = true;
                        int j = 0;
                        while (str.charAt(j) != '(') {
                            cons += str.charAt(j);
                            j++;
                            if (j == str.length()) {
                                chk = false;
                                break;
                            }
                        }
                        String src = "";
                        int k = 0;
                        while (k != str.length()) {
                            if (str.charAt(k) == ')') {
                                src = str.substring(k - 3, k);
                                // System.out.println(src);
                            }
                            k++;
                        }
                        if (chk) {
                            mci = mcass.itype(ins, des, src, cons);
                        } else {
                            mci = mcass.itype(ins, des, cons, "0");
                        }

                    } else {
                        String des = line[1];
                        String src = line[2];
                        String cons = line[3];

                        mci = mcass.itype(ins, des, src, cons);
                    }

                    for (int i = 0; i < mci.length - 1; i++) {
                        System.out.print(mci[i] + " ");
                    }
                    System.out.println();
                    System.out.print("0x" + mci[mci.length - 1]);
                    // itype ending
                } else if (ins.equalsIgnoreCase("j") || ins.equalsIgnoreCase("jal")) {
                    // jtype beginning
                    String[] mcj = new String[2];
                    String address = line[1];
                    mcj = mcass.jtype(ins, address);
                    for (int i = 0; i < mcj.length - 1; i++) {
                        System.out.print(mcj[i] + " ");
                    }
                    System.out.println();
                    System.out.print("0x" + mcj[mcj.length - 1]);
                    // jtype ending
                } else {
                    System.out.println("Check your code, there must be an error!!");
                }
                System.out.println();
                line = scan.nextLine().split(" ");
                ins = line[0];
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
