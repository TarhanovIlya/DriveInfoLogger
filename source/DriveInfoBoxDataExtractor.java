import org.jsoup.nodes.Element;

import java.time.LocalTime;

public class DriveInfoBoxDataExtractor {
    public DriveInfoBoxDataExtractor() {
    }

    public LocalTime fetchTime(Element element){
        Element timeElement = element;

        int[] load = {0,0,0,0,0,0,0};
        if(fancyElement(timeElement)){
            timeElement = skipFancyShell(timeElement);
        }

        for(int i=0;i< load.length;i++){
            timeElement = timeElement.child(load[i]);
        }

        LocalTime time = LocalTime.parse(timeElement.text());

        return  time;
    }

    public  String fetchFrom(Element element){
        Element fromElement = element;

        int[] load = {0,0,0,0,0,1};
        if(fancyElement(fromElement)){
            fromElement = skipFancyShell(fromElement);
        }

        for(int i=0;i< load.length;i++){ //0,1,0,0,0,0,1
            fromElement = fromElement.child(load[i]);
        }

        return fromElement.text();
    }

    public String fetchTo(Element element){
        Element toElement = element;

        int[] load = {0,0,0,1,0,1};
        if(fancyElement(toElement)){
            toElement = skipFancyShell(toElement);
        }

        for(int i=0;i< load.length;i++){
            toElement = toElement.child(load[i]);
        }

        return toElement.text();
    }

    public int fetchAvailableSeats(Element element){

        if(fancyElement(element)){
            element = skipFancyShell(element);
        }

        Element seatsElement = element;

        int[] load = {0,0,0,2,0,3};




        try {
            for(int i=0;i< load.length;i++){
                seatsElement = seatsElement.child(load[i]);
            }
        }
        catch (Exception e){

            if(e.getClass() == IndexOutOfBoundsException.class)
            {
                Element noSeatsElement = element;
                load =new int[]{0,0,0,3,0,0};
                for(int i=0;i< load.length;i++){
                    noSeatsElement = noSeatsElement.child(load[i]);
                }

                if (noSeatsElement.text().toLowerCase().contains("нет мест")) return 0;
            }

            else throw e;
        }


        String seatsElementText = seatsElement.text();

        if (!seatsElementText.toLowerCase().contains("последнее"))
        {
            int index = seatsElementText.indexOf("мест");
            return Integer.parseInt(seatsElementText.substring(index-3,index).replace('+',' ').trim());
        }
        else return 1;


    }

    public String fetchDate(String url){
        int index = url.indexOf("date");
        String date1 = url.substring(index+5,index+15);
        String[] dateContent = new String[3];
        dateContent = date1.split("-");
        return dateContent[2]+"-"+dateContent[1]+"-"+dateContent[0];
    }

    private boolean fancyElement(Element element){
        return element.child(0).child(0).hasAttr("style");
    }

    private Element skipFancyShell(Element element){
        element = element.child(0);
        element = element.child(1);
        return  element;
    }

}
