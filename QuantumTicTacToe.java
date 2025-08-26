import java.util.*;

public class Main {

    static class QuantumTicTacToe {
        private final List<String>[] qBoard = new ArrayList[9];
        private final char[] cBoard = new char[9];
        private final List<Integer>[] adj = new ArrayList[9];
        private final Map<String, int[]> moveEndpoints = new LinkedHashMap<>();
        private int xMoveNo = 1, oMoveNo = 1;
        private final Scanner in;

        @SuppressWarnings("unchecked")
        QuantumTicTacToe(Scanner in) {
            this.in = in;
            for (int i = 0; i < 9; i++) {
                qBoard[i] = new ArrayList<>();
                cBoard[i] = ' ';
                adj[i] = new ArrayList<>();
            }
        }

        private String rc(int cell) {
            return "(" + (cell / 3) + "," + (cell % 3) + ")";
        }

        private void addEdge(int a, int b) {
            if (!adj[a].contains(b)) adj[a].add(b);
            if (!adj[b].contains(a)) adj[b].add(a);
        }

        private void removeEdge(int a, int b) {
            adj[a].remove(Integer.valueOf(b));
            adj[b].remove(Integer.valueOf(a));
        }

        private boolean isCellIndexValid(int c) {
            return c >= 0 && c < 9;
        }

        private boolean isClassicalOccupied(int c) {
            return cBoard[c] == 'X' || cBoard[c] == 'O';
        }

        public void printBoard() {
            System.out.println("\nBoard:");
            for (int i = 0; i < 9; i++) {
                String cellText;
                if (cBoard[i] != ' ') {
                    cellText = " " + cBoard[i] + " ";
                } else if (qBoard[i].isEmpty()) {
                    cellText = "[" + i + "]";
                } else {
                    cellText = qBoard[i].toString();
                }
                System.out.printf("%-10s", cellText);
                if ((i + 1) % 3 == 0) System.out.println();
            }
            System.out.println();
        }

        public boolean playGame() {
            char current = 'X';
            while (true) {
                printBoard();
                if (isDraw()) {
                    System.out.println("Result: Draw! No more playable moves.");
                    return false;
                }
                String label = (current == 'X') ? "X" + (xMoveNo++) : "O" + (oMoveNo++);
                System.out.println("Player " + current + " turn. Place quantum move " + label + " in two cells (0-8).");

                int a = askCell("First cell: ");
                int b = askCell("Second cell: ");
                if (a == b || !isCellIndexValid(a) || !isCellIndexValid(b) || isClassicalOccupied(a) || isClassicalOccupied(b)) {
                    System.out.println("❌ Invalid move. Try again.");
                    if (current == 'X') xMoveNo--;
                    else oMoveNo--;
                    continue;
                }

                qBoard[a].add(label);
                qBoard[b].add(label);
                moveEndpoints.put(label, new int[]{a, b});
                List<Integer> path = findPath(a, b);
                addEdge(a, b);

                if (path != null) {
                    Set<Integer> cycleNodes = new LinkedHashSet<>(path);
                    cycleNodes.add(a);
                    cycleNodes.add(b);
                    System.out.println("⚡ Cycle detected involving cells: " + cycleNodes);
                    char opponent = (current == 'X') ? 'O' : 'X';
                    int collapseCell = askCollapseChoice(opponent, cycleNodes);
                    collapseAt(collapseCell);
                    char winner = checkWinner();
                    if (winner != ' ') {
                        printBoard();
                        System.out.println("🏆 Player " + winner + " wins!");
                        return true;
                    }
                }

                current = (current == 'X') ? 'O' : 'X';
            }
        }

        private int askCell(String prompt) {
            while (true) {
                System.out.print(prompt);
                String line = in.nextLine().trim();
                try {
                    int c = Integer.parseInt(line);
                    if (c >= 0 && c < 9) return c;
                } catch (NumberFormatException ignored) {}
                System.out.println("Please enter a number from 0 to 8.");
            }
        }

        private int askCollapseChoice(char opponent, Set<Integer> cycleNodes) {
            System.out.println("Cycle formed. Player " + opponent + " chooses collapse cell from: " + cycleNodes);
            while (true) {
                int choice = askCell("Collapse cell: ");
                if (cycleNodes.contains(choice) && !isClassicalOccupied(choice)) return choice;
                System.out.println("Choose a valid cell from the cycle that is not already classical.");
            }
        }

        private void collapseAt(int cell) {
            String chosenLabel = chooseLabelForCollapse(cell);
            if (chosenLabel == null && !qBoard[cell].isEmpty()) chosenLabel = qBoard[cell].get(qBoard[cell].size() - 1);
            if (chosenLabel == null) return;
            char classicalMark = chosenLabel.charAt(0);
            cBoard[cell] = classicalMark;

            List<String> labelsHere = new ArrayList<>(qBoard[cell]);
            for (String lab : labelsHere) {
                int[] endpoints = moveEndpoints.get(lab);
                if (endpoints == null) continue;
                int u = endpoints[0], v = endpoints[1];
                qBoard[u].remove(lab);
                qBoard[v].remove(lab);
                removeEdge(u, v);
                moveEndpoints.remove(lab);
            }

            qBoard[cell].clear();
            System.out.println("⬇️  Collapsed " + rc(cell) + " to classical '" + classicalMark + "'.");
        }

        private String chooseLabelForCollapse(int cell) {
            String best = null;
            int bestMoveNum = -1;
            for (String lab : qBoard[cell]) {
                int[] ep = moveEndpoints.get(lab);
                if (ep == null) continue;
                int a = ep[0], b = ep[1];
                boolean edgeAlive = adj[a].contains(b) && adj[b].contains(a);
                if (!edgeAlive) continue;
                int num = parseMoveNumber(lab);
                if (num > bestMoveNum) {
                    bestMoveNum = num;
                    best = lab;
                }
            }
            return best;
        }

        private int parseMoveNumber(String lab) {
            try { return Integer.parseInt(lab.substring(1)); }
            catch (Exception e) { return 0; }
        }

        private List<Integer> findPath(int src, int dst) {
            boolean[] vis = new boolean[9];
            int[] parent = new int[9];
            Arrays.fill(parent, -1);
            Deque<Integer> dq = new ArrayDeque<>();
            dq.add(src); vis[src] = true;

            while (!dq.isEmpty()) {
                int u = dq.poll();
                if (u == dst) break;
                for (int v : adj[u]) {
                    if (!vis[v]) { vis[v] = true; parent[v] = u; dq.add(v); }
                }
            }

            if (!vis[dst]) return null;
            List<Integer> path = new ArrayList<>();
            for (int cur = dst; cur != -1; cur = parent[cur]) path.add(cur);
            Collections.reverse(path);
            return path;
        }

        private char checkWinner() {
            int[][] lines = { {0,1,2},{3,4,5},{6,7,8}, {0,3,6},{1,4,7},{2,5,8}, {0,4,8},{2,4,6} };
            for (int[] L : lines) {
                char a = cBoard[L[0]], b = cBoard[L[1]], c = cBoard[L[2]];
                if (a != ' ' && a == b && b == c) return a;
            }
            return ' ';
        }

        private boolean isDraw() {
            int classicalCount = 0, freeCells = 0;
            for (int i = 0; i < 9; i++) {
                if (cBoard[i] != ' ') classicalCount++;
                else freeCells++;
            }
            return classicalCount == 9 || freeCells < 2;
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        QuantumTicTacToe game = new QuantumTicTacToe(in);
        game.playGame();
    }
}
