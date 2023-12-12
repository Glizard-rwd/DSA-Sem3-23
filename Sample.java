class SCGuesser {
    private Sample sample;

    public SCGuesser() {
        this.sample = new Sample();
    }

    public void start() {
        String key = "MMMMMMMMMMMM";
        int steps = 0;

        while (!key.equals(sample.correctKey)) {
            key = modifyStringToMatch(key, sample.correctKey);
            steps++;
        }

        System.out.println("I found the secret key. It is " + key);
        System.out.println("Number of steps: " + steps);

        // After finding the correct key, check and print the number of total guesses
        int matched = sample.guess(key);
        if (matched == sample.correctKey.length()) {
            System.out.println("Number of guesses: " + sample.counter);
        }
    }

    public String modifyStringToMatch(String str1, String str2) {
        int m = str1.length();
        int n = str2.length();

        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(Math.min(dp[i][j - 1], dp[i - 1][j]), dp[i - 1][j - 1]);
                }
            }
        }

        StringBuilder modifiedKey = new StringBuilder(str1);
        int i = m, j = n;
        int steps = 0; // Counter to track steps

        while (i > 0 && j > 0) {
            if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                i--;
                j--;
            } else if (dp[i - 1][j - 1] <= dp[i - 1][j] && dp[i - 1][j - 1] <= dp[i][j - 1]) {
                modifiedKey.setCharAt(i - 1, str2.charAt(j - 1));
                i--;
                j--;
                steps++; // Increment steps for substitution
            } else if (dp[i][j - 1] <= dp[i - 1][j]) {
                modifiedKey.insert(i, str2.charAt(j - 1));
                j--;
                steps++; // Increment steps for insertion
            } else {
                modifiedKey.deleteCharAt(i - 1);
                i--;
                steps++; // Increment steps for deletion
            }
            System.out.println("Guessing..." + modifiedKey);
        }

        System.out.println("Number of modification steps: " + steps); // Print the number of steps
        return modifiedKey.toString();
    }

    // Other methods remain the same
}
public class Sample {
    String correctKey;
    int counter;

    public Sample() {
        // for the real test, your program will not know this
        correctKey = "OMCHACMOHAMO";
        counter = 0;
    }

    public int guess(String guessedKey) {
        counter++;
        // validation
        if (guessedKey.length() != correctKey.length()) {
            return -1;
        }
        int matched = 0;
        for (int i = 0; i < guessedKey.length(); i++) {
            char c = guessedKey.charAt(i);
            if (c != 'M' && c != 'O' && c != 'C' && c != 'H' && c != 'A') {
                return -1;
            }
            if (c == correctKey.charAt(i)) {
                matched++;
            }
        }
        if (matched == correctKey.length()) {
            System.out.println("Number of guesses: " + counter);
        }
        return matched;
    }

    public static void main(String[] args) {
        new SCGuesser().start();
    }
}

