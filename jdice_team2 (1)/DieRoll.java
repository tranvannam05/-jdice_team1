import java.util.Random;

/*
JDice: Java Dice Rolling Program
Copyright (C) 2006 Andrew D. Hilton  (adhilton@cis.upenn.edu)

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License...
*/

public class DieRoll {
    private int ndice;       // Số lượng xúc xắc sẽ được tung
    private int nsides;      // Số mặt của mỗi xúc xắc
    private int bonus;       // Điểm cộng thêm sau khi tung xúc xắc

    private static Random rnd;

    // Khối static khởi tạo biến ngẫu nhiên một lần duy nhất
    static {
        rnd = new Random();
    }

    // Constructor khởi tạo đối tượng với số xúc xắc, số mặt, và điểm thưởng
    public DieRoll(int ndice, int nsides, int bonus) {
        this.ndice = ndice;
        this.nsides = nsides;
        this.bonus = bonus;
    }

    // Thực hiện việc tung xúc xắc và trả về kết quả
    public RollResult makeRoll() {
        RollResult result = new RollResult(bonus);  // Tạo đối tượng kết quả với bonus ban đầu
        for (int i = 0; i < ndice; i++) {
            int roll = rnd.nextInt(nsides) + 1;     // Tạo số ngẫu nhiên từ 1 đến nsides
            result.addResult(roll);                 // Thêm kết quả tung vào kết quả tổng
        }
        return result;
    }

    // Xuất biểu diễn dạng chuỗi của cú tung xúc xắc, ví dụ "3d6+2"
    public String toString() {
        String result = ndice + "d" + nsides;
        if (bonus > 0) {
            result += "+" + bonus;
        } else if (bonus < 0) {
            result += bonus;  // bonus đã có dấu âm
        }
        return result;
    }
}
