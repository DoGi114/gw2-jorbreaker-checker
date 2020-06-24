import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {
        System.out.println("wait a bit for program to process all the information :)");

        GW2DAO gw2DAO = new GW2DAO();

        JSONArray guildLog = gw2DAO.getGuildLog();
        Map<String, Integer> donations = gw2DAO.getJorbreakerCompetitionLeaderboard(guildLog);
        printResults(donations);

    }

    private static void printResults(Map<String, Integer> donations){

        LinkedHashMap<String, Integer> sortedMap = donations.entrySet()
                .stream()
                .sorted((Map.Entry.<String, Integer>comparingByValue().reversed()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        System.out.println("--------------------------------------------------");
        for(String ign : sortedMap.keySet()){
            StringBuilder name = new StringBuilder();
            StringBuilder amount = new StringBuilder();
            name.append("|");
            name.append(ign);
            for(int i = name.length(); i < 38; i++){
                name.append(" ");
            }
            name.append("|");
            amount.append(sortedMap.get(ign));
            for(int i = amount.length(); i < 10; i++){
                amount.append(" ");
            }
            amount.append("|");
            System.out.println(name.toString() + amount.toString());
        }
        System.out.println("--------------------------------------------------");
        System.out.println("Press enter to exit the program.");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }


}
