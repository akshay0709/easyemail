public class Runn {
    public static void main(String[] args){
        EasyEmail em = new EasyEmail("smtp.mailtrap.io", "2525", true, "SSL");
        em.setIdentity("ca20597386bdd9", "46e03d0b16dfb7");
        em.send("b450751c2e-e4ca64@inbox.mailtrap.io", "Hi", "Wassup Akshay");
    }
}
