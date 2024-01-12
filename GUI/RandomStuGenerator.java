package GUI;

import java.util.Random;
/*
  生成随机对象
 */
public class RandomStuGenerator {
    private static final String[] SURNAMES = {"王", "李", "张", "刘", "陈", "杨", "赵", "黄","蒋","吴","戴","孔","孔","罗",
    "彭","曾","蔡","周"};

    private static final String[] NAMES = {"小明", "小红", "小强", "丽丽", "建华", "美美", "超超", "雅雅","志伟","成","誉誉","飞飞"
    ,"龙龙","钰钰","迪迪"};
    private static final String[] DATES = {"1990-01-01", "1995-05-10", "2000-08-15", "1985-03-22", "1998-11-30"};
    private static final String[] SEXES = {"男", "女"};

    private Random random;

    public RandomStuGenerator() {
        this.random = new Random();
    }

    public Stu generateRandomStu() {
        Stu stu = new Stu();
        stu.setId(getRandomId()); // Assuming some range for id
        stu.setName(getRandomElement(SURNAMES) + getRandomElement(NAMES));
        stu.setDate(getRandomElement(DATES));
        stu.setSex(getRandomElement(SEXES));
        stu.setPhone(generateRandomPhone());
        return stu;
    }

    private String getRandomElement(String[] array) {
        int randomIndex = random.nextInt(array.length);
        return array[randomIndex];
    }
    private Integer getRandomId(){
        StringBuilder Id=new StringBuilder("2022401");
        for (int i = 0; i < 3; i++) {
            Id.append(random.nextInt(10));
        }
        // 将 StringBuilder 转换为 Integer
        return Integer.parseInt(Id.toString());
    }
    private String generateRandomPhone() {
        StringBuilder phone = new StringBuilder("1");
        for (int i = 0; i < 4; i++) {
            phone.append(random.nextInt(10));
        }
        return phone.toString();
    }
}
