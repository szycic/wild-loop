package org.wildloop;

/**
 * Reprezentuje niezmienną pozycję w dwuwymiarowej przestrzeni.
 * Zaimplementowana jako record ze względu na większą zwięzłość.
 *
 * @param x współrzędna pozioma pozycji
 * @param y współrzędna pionowa pozycji
 */
public record Position(int x, int y) {

    /**
     * Wyznacza główny kierunek do wskazanej pozycji docelowej.
     * Wybiera kierunek EAST/WEST, jeśli różnica w x jest większa,
     * lub NORTH/SOUTH, jeśli różnica w y jest większa lub równa.
     *
     * @param target pozycja docelowa
     * @return kierunek prowadzący najkrótszą drogą do celu
     */
    public Direction directionTo(Position target) {
        int dx = Integer.compare(target.x() - this.x, 0);
        int dy = Integer.compare(target.y() - this.y, 0);

        if (Math.abs(dx) > Math.abs(dy)) {
            return dx > 0 ? Direction.EAST : Direction.WEST;
        } else {
            return dy > 0 ? Direction.SOUTH : Direction.NORTH;
        }
    }

    /**
     * Wyznacza główny kierunek od wskazanej pozycji źródłowej.
     * Działa analogicznie do {@link #directionTo(Position)}, ale z odwróconymi kierunkami.
     *
     * @param target pozycja źródłowa
     * @return kierunek prowadzący od źródła do bieżącej pozycji
     */
    public Direction directionFrom(Position target) {
        int dx = Integer.compare(this.x - target.x(), 0);
        int dy = Integer.compare(this.y - target.y(), 0);

        if (Math.abs(dx) > Math.abs(dy)) {
            return dx > 0 ? Direction.WEST : Direction.EAST;
        } else {
            return dy > 0 ? Direction.NORTH : Direction.SOUTH;
        }
    }

    /**
     * Oblicza odległość do pozycji docelowej.
     * Jest to suma bezwzględnych różnic współrzędnych x i y.
     *
     * @param target pozycja docelowa
     * @return |x1-x2| + |y1-y2|
     */
    public int distanceTo(Position target) {
        return Math.abs(this.x - target.x()) + Math.abs(this.y - target.y());
    }

    /**
     * Tworzy nową pozycję przesuniętą o jeden krok w zadanym kierunku.
     * Ze względu na niemutowalność rekordu, zwraca nowy obiekt, zamiast
     * modyfikować istniejący.
     *
     * @param direction kierunek przemieszczenia
     * @return nowa pozycja po wykonaniu ruchu
     */
    public Position newPosition(Direction direction) {
        return new Position(this.x + direction.getDx(), this.y + direction.getDy());
    }
}