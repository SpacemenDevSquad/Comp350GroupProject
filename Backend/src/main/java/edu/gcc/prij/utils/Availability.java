package edu.gcc.prij.utils;

import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.gcc.prij.objects.section.Section;
import edu.gcc.prij.utils.time.TimeHelper;
import edu.gcc.prij.utils.time.Timeslot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Availability {
    private Map<Character, List<Tuple<Integer, Integer>>> availability;
    private static final List<Character> days = List.of('M', 'T', 'W', 'R', 'F');

    public Availability(String jsonAvailability){
        ObjectMapper mapper = new ObjectMapper();
        availability = new HashMap<>();

        try {
            JsonNode rootNode = mapper.readTree(jsonAvailability);

            for (Character day : days) {
                JsonNode dailyAvailabilityJsonNode = rootNode.get(day.toString());

                List<Tuple<Integer, Integer>> dailyAvailability = new ArrayList<>();

                System.out.println(dailyAvailabilityJsonNode);
                for (JsonNode timeRange : dailyAvailabilityJsonNode) {
                    assert(timeRange.size() == 2);
                    dailyAvailability.add(new Tuple<>(
                        timeRange.get(0).asInt(),
                        timeRange.get(1).asInt()
                    ));
                }

                availability.put(day, dailyAvailability);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Availability(Map<Character, List<Tuple<Integer, Integer>>> availability){
        this.availability = availability;
    }

    public Availability(
        List<Tuple<Integer, Integer>> monday,
        List<Tuple<Integer, Integer>> tuesday,
        List<Tuple<Integer, Integer>> wednesday,
        List<Tuple<Integer, Integer>> thursday,
        List<Tuple<Integer, Integer>> friday
    ){
        availability = new HashMap<>();
        availability.put('M', monday);
        availability.put('T', tuesday);
        availability.put('W', wednesday);
        availability.put('R', thursday);
        availability.put('F', friday);
    }

    public boolean availableDuringTimeslot(Timeslot timeslot){
        List<Tuple<Integer, Integer>> daySlots = availability.get(timeslot.getDay());
        if (daySlots == null) return false;
        for (Tuple<Integer, Integer> ts : daySlots){
            // Student available [ts.x, ts.y], class [timeslot.start, timeslot.end]
            if (timeslot.getStartTime() >= ts.x && timeslot.getEndTime() <= ts.y){
                return true;
            }
        }
        
        return false;
    }

    public boolean availableForSection(Section section){
        Timeslot[] timeslots = section.getTimeslots();

        for (Timeslot timeslot : timeslots){
            if(!availableDuringTimeslot(timeslot)){
                return false;
            }
        }

        return true;
    }

    public Map<Character, List<Tuple<Integer, Integer>>> getAvailability(){
        return availability;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (char day : days){
            sb.append(day + " ");
            for (Tuple<Integer, Integer> ts : availability.get(day)){
                sb.append(TimeHelper.timeToString(ts.x));
                sb.append("-");
                sb.append(TimeHelper.timeToString(ts.y));
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
