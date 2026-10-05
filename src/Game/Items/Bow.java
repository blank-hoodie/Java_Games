package Game.Items;

public class Bow {
    private int arrows; // сколько стрел осталось

    public Bow(int arrows) {
        this.arrows = arrows;
    }

    public int getArrows() {
        return arrows;
    }

    // выстрел: если стрелы есть — тратим одну и возвращаем true
    public boolean shoot() {
        if (arrows <= 0) {
            return false;
        }
        arrows--;
        return true;
    }

    // подобрать стрелы
    public void addArrows(int count) {
        arrows += count;
    }
}
