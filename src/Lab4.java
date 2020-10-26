import java.io.*;
import java.util.Scanner;

//////////////
/*
class Song alike Link class
 */
class Song {
    protected String track;
    private String artist;
    protected Song next;

    /* constructor
    param t name of the track
    param a name of the artist
    */
    public Song(String t, String a) {
        track = t;
        artist = a;
    }

    /* displaySong() method returns name of the track followed by artist name
    return@ name of the track followed by artist name
    */
    public String displaySong() {
        return "\"" + track + "\"" + " by " + artist;
    }
}  //end class Song
///////////////////////
/*
class Playlist alike Linklist class
 */
class Playlist {
    private Song first = null;
    private Song currentSong = first;

    /* isEmptry() method determines if play list if empty
    return@ true if empty, false if not empty
    */
    public boolean isEmpty() {
        return first == null;
    }

    /* addSong() method adds new song to the list
    param@ t name of the track
    param@ a name of the artist
    */
    public void addSong(String t, String a){
        Song newSong = new Song(t,a);
        if(isEmpty())
            first = newSong;
        else if(newSong.track.compareToIgnoreCase(first.track) < 0) {
            newSong.next = first;
            first = newSong;
        }
        else{
            Song current = first;
            while(!newSong.track.equals(current.track)){
                if(current.next == null){
                    current.next = newSong;
                    break;
                }
                else if(current.track.equals(t) ||
                        current.next.track.equals(t)){
                    break;
                }
                else if(current.next.track.compareToIgnoreCase(newSong.track) > 0){
                    Song temp = current.next;
                    current.next = newSong;
                    newSong.next = temp;
                }
                current = current.next;
            }
        }
    }

    /* listenToNextSong() method returns name of the song and
    name of the artist ordered to play
    return@ name of the song and name of the artist ordered to play
     */
    public String listenToNextSong(){
        String s = first.displaySong();
        if(first.next == null)
            first = null;
        first = first.next;
        return s;
    }

    /* displayList() displays all the song in the list with corresponding
    artist
     */
    public void displayList(){
        Song current = first;
        int number = 1;
        while(current != null){
            System.out.print(number + " " + current.displaySong());            ;
            current = current.next;
            System.out.println("");
            number++;
        }
        System.out.println("");
    }

    /* printInTextFile() prints all the song in the list with corresponding
    artist into the .txt file
    param@ path String name of the .txt file to be printed in
     */
    public void printInTextFile(String path) throws IOException{
        PrintWriter pr = new PrintWriter(path);
        Song current = first;
        int number = 1;
        pr.println("Downloaded list of Songs is sorted in ascending order:");
        while(current != null){
            pr.println(number + ". " + current.displaySong());            ;
            current = current.next;
            number++;
        }
        pr.close();
    }
} //end of Playlist class
///////////////
/*
additional class for recording played history.
array data structure is used to store song names with
corresponding artist name
 */
class PlayedHistory{
    private String[] history = new String[100];
    private int pointer = 0;

    public void addSong(String played){
        history[pointer++] = played;
    }
    public void showList(){
        System.out.println("\nPLayed: ");
        for(int i = pointer-1; i >= 0; i--)
            System.out.println(history[i]);
    }
} // end of PlayedHistory class
////////////////////////
/* MyQueue alike Queue class with implementing
Playlist class slike Linklist class
 */
class MyQueue {
    private Playlist myList;
    private PlayedHistory history = new PlayedHistory();

    /* construction method
     */
    public MyQueue(){
        myList = new Playlist();
    }

    /* isEmpty() method determines if queue if empty
    return@ true if empty, false if not empty
    */
    public boolean isEmpty(){
        return myList.isEmpty();
    }

    /* addSong() method adds new song to the queue
    param@ s name of the track
    param@ a name of the artist
    */
    public void addSong(String s, String a){
        myList.addSong(s, a);
    }

    /* displayQueue() method displays the queue nodes
    */
    public void displayQueue(){
        System.out.println("Songs are arranged in ascending order:");
        myList.displayList();
    }

    /* printInTextFile() method prints Playlist songs into
    the .txt file
    param@ path String name of the .txt file to be printed in
     */
    public void printInTextFile(String path) throws IOException{
        myList.printInTextFile(path);
    }

    /* playNext() playes the first song in the list, i.e.
    prints the name of the song and artist into the
    console line. Also it adds played song to the history
     */
    public void playNext(){
        String str = myList.listenToNextSong();
        history.addSong(str);
        System.out.println("playing " + str);
    }

    /* showHistory() method displayed all played songs
     */
    public void showHistory(){
        history.showList();
    }
} // end of MyQueue class
////////////////
/* Lab4 class is the class with main method
 */
public class Lab4 {
    /* main method
     */
    public static void main(String[] args) throws IOException {
        // creating of "English top 200" queue for last 16 weeks
        // song will not be repeated in the list
        // will be arranged in ascending order
        MyQueue EnglishTop = new MyQueue();
        String path = "C:\\Users\\ayram\\Desktop\\Data Structure\\IdeaProjects\\HomeWork\\Lab4\\data\\";
        String line = "";
        // 16 last weeks
        // 16 .csv files will be read.
        for (int i = 1; i <= 16; i++) {
            try {
                String s = "week" + i + ".csv";
                BufferedReader reader = new BufferedReader(new FileReader(path + s));
                // first 2 lines are description before the table
                //that's why I've read them and have not stored in an array
                reader.readLine();
                reader.readLine();
                while ((line = reader.readLine()) != null) {
                    String[] array = line.split(",");
                    if(array[1].charAt(0) == '"') {
                        array[1] = array[1].substring(1, array[1].length()-1);
                    }
                    if(array[2].charAt(0) == '"') {
                        array[2] = array[2].substring(1, array[2].length()-1);
                    }
                    EnglishTop.addSong(array[1], array[2]);
                }
                reader.close();
            } catch (IOException ep) {
                ep.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("No such file exists.");
            }
        }
        // printing the created queue in the TotalList.txt
        EnglishTop.printInTextFile(path + "TotalList.txt");
        // playing first 6 songs of the queue
        EnglishTop.playNext();
        EnglishTop.playNext();
        EnglishTop.playNext();
        EnglishTop.playNext();
        EnglishTop.playNext();
        EnglishTop.playNext();
        // showing played history
        EnglishTop.showHistory();
    }
} // end of Lab4 class

