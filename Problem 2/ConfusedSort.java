import java.io.*;
import java.util.Scanner;


// command line must have input and output txt files after compilation. 
public class ConfusedSort {
    public static void main (String [] args) throws IOException
    {
        if (args.length < 2)
        {
            System.out.println("there was an error when attempting to assign input and output files. \n Please review the readMe file and try again.");
            return; 
        }
        File in = new File(args[0]);
        File out = new File(args[1]);
        // clear the out file of any old outputs
        FileWriter fw = new FileWriter(out);
        try( BufferedWriter writer = new BufferedWriter(fw)){
            writer.flush();
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        // begin to read the file and conduct the confused sorting, then output results.  
        readFile(in, out);
    }

    /**
     * Making the linked list for a stack, will be used as a ConfusedStack object
     * each node consists of String symbol and a next node. 
     */
    public class Node{
        String symbol;
        Node next; 

        public Node(String symbol)
        {
            this.symbol = symbol;
            this.next = null;
        }
    }

     // initializing the list
     Node head = null; 
     Node tail = null;   
     int listLength =0;   
     
     // making the output file easily accessible for any part of a ConfusedSort
     public File outputFile = null;


    //sorting the elements of the existing, unsorted list
     public void sortList() {    
        Node current = head, index = null;  
        String temp;  
          
        if(head == null) {  
            return;  
        }  
        else {  
            while(current != null) {  
                index = current.next;  
                while(index != null) {  
                    float currenInt = Float.parseFloat(current.symbol);
                    float indexInt = Float.parseFloat(index.symbol);
                    if( currenInt <= indexInt ) 
                    {  
                        temp = current.symbol;  
                        current.symbol = index.symbol;  
                        index.symbol = temp;  
                    }  
                    index = index.next;  
                }  
                current = current.next;  
            }      
        }  
    } 
    
    // the same as above, but backwards
    public void reverseSort() {    
        Node current = head, index = null;  
        String temp;  
          
        if(head == null) {  
            return;  
        }  
        else {  
            while(current != null) {  
                index = current.next;  
                while(index != null) {  
                    float currenInt = Float.parseFloat(current.symbol);
                    float indexInt = Float.parseFloat(index.symbol);
                    if( currenInt >= indexInt) 
                    {  
                        temp = current.symbol;  
                        current.symbol = index.symbol;  
                        index.symbol = temp;  
                    }  
                    index = index.next;  
                }  
                current = current.next;  
            }      
        }  
    } 

     /**
      * insert method 
      * adds the data to a new node at the end (tail) of the list
      * @param number
      */
     public void insert(String symbol)
     {
        Node newNode = new Node (symbol);
        if (listLength==0)
        {
            head = tail = newNode; 
        }
        else
        {
            tail.next = newNode; 
            tail = newNode; 
        }
        listLength++; 
     }

     /**
      * removes all instances of 666 from the list, regardless of number or position
      */
     public void remove666 ()
     {
        Node toRemove = head; 
        Node beforeRemove = null;
        if (toRemove.symbol.equals("666") )
        {
            head = head.next; 
        }
        toRemove = toRemove.next; 
        while (toRemove.next !=null )
        {
            if (toRemove.symbol.equals("666"))
            {
                beforeRemove.next = toRemove.next; 
            }
            beforeRemove = toRemove; 
            toRemove = toRemove.next;
        }
     }

     /**
      * converts the line from the symbol to a float value that can be easily sorted by value
      * @param line
      * @param list
      * @return
      */
     public static boolean insertSymbol(String line, ConfusedSort list)
     {
        if (line.equals("Do"))
        {
            list.insert("0.5");
            return true; 
        }
        else if (line.equals("Re"))
        {
            list.insert("100.5");
            return true;
        }
        else if (line.equals("Mi"))
        {
            list.insert("1000.5");
            return true;
        }
        else if (line.equals("&"))
        {
            list.insert("3.5");
            return true;
        }
        else if (line.equals("@"))
        {
            list.insert("3.4");
            return true;
        }
        else if (line.equals("%"))
        {
            list.insert("1005000.5");
            return true;
        }
        else if (line.equals("Asymbolwithareallylongname"))
        {
            list.insert("55.5");
            return true;
        }
        else if (line.equals("$"))
        {
            list.insert("20.5");
            return true;
        }
        else if (line.equals("Fa"))
        {
            list.insert("15.5");
            return true;
        }
        else if (line.equals("One"))
        {
            list.insert("103.1");
            return true;
        }
        else if (line.equals("Two"))
        {
            list.insert("103.2");
            return true;
        }
        else if (line.equals("Three"))
        {
            list.insert("103.3");
            return true;
        }

        // ensure that if not a symbol, is a natural number
        // theLine < 2147483647 may be omitted depending on the verdict on the largest natural number
        if (line.matches("[0-9]+") && !(line.startsWith("0") && line.length() > 1))
        {
            list.insert(line);
            return true;
        }
        return false; 
    }

    // this is the powerhouse, handles inputs, sorting and output calls 
     public static void readFile( File input, File out) throws IOException
    {
        ConfusedSort list = new ConfusedSort();
        list.outputFile = out;  
        try( Scanner inputScanning = new Scanner(input)){ 
            String line = null; 
            boolean moveAlong = true;
            while (inputScanning.hasNextLine() && moveAlong)
            {
                line = inputScanning.nextLine();
                if (line.equals("") )
                {
                    output(out, "Input error.");
                    return; 
                }
                moveAlong = insertSymbol(line, list);
                if (moveAlong == false )
                {
                    output(out, "Input error.");
                    return; 
                }
            }
            inputScanning.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        if (list.search())
        {
            // there is "666" in the list, add @ and remove all 666, then sort reversed
            insertSymbol("@", list);
            list.remove666();
            list.reverseSort();
        }
        else 
        {
            // regular sort
            list.sortList();
        }
        // convert from floats to symbols
        list.toSymbols(); 

        // begin to move through the sorted list and output each symbol/ integer
        Node outNode = list.head; 
        while (outNode != null )
        {
            output(out, outNode.symbol);
            outNode= outNode.next; 
        }
    }

    /**
     *  writes every node of the list in its own line, followed by the linefeed character, 
     *  this works becuase linux servers consider '\n' as only 'LF', whereas windows considers it 
     *  'CR LF'
     *  because of this there will be no empty line on linux, becuase linefeed does not create a new line,
     *  but instead tells the machine to put the next bytes on the next line
     */
    public static void output(File output, String number) throws IOException 
    {
        FileWriter fw = new FileWriter(output, true);
        byte lineFeed = 10;
        try( BufferedWriter writer = new BufferedWriter(fw)){
            writer.write(number);
            writer.write(lineFeed);
            writer.flush();
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * searches for a single instance of 666 in the list
     * @return true if found
     */
    public boolean search()
    {
        Node current = this.head; 
        while (current!= null)
        {
            if (current.symbol.equals( "666") )
            {
                return true; // one found
            }
            current = current.next; 
        }
        return false;  // none found
    }

    /**
     * converts all the non-int floats in the sorted list into their origional 
     * confused symbols
     */
    public void toSymbols()
    {
        Node searching = this.head;
        // looks for symbols corresponding floats in the entire list 
        while (searching !=null )
        {
            if (searching.symbol.equals("0.5"))
            {
                searching.symbol = "Do";
            }
            else if ( searching.symbol.equals("100.5"))
            {
                searching.symbol = "Re";
            }
            else if ( searching.symbol.equals("1000.5"))
            {
                searching.symbol = "Mi";
            }
            else if ( searching.symbol.equals("3.4"))
            {
                searching.symbol = "@";
            }
            else if ( searching.symbol.equals("3.5"))
            {
                searching.symbol = "&";
            }
            else if ( searching.symbol.equals("1005000.5"))
            {
                searching.symbol = "%";
            }
            else if ( searching.symbol.equals("55.5"))
            {
                searching.symbol = "Asymbolwithareallylongname";
            }
            else if ( searching.symbol.equals("20.5"))
            {
                searching.symbol = "$";
            }
            else if ( searching.symbol.equals("15.5"))
            {
                searching.symbol = "Fa";
            }
            else if ( searching.symbol.equals("103.1"))
            {
                searching.symbol = "One";
            }
            else if ( searching.symbol.equals("103.2"))
            {
                searching.symbol = "Two";
            }
            else if ( searching.symbol.equals("103.3"))
            {
                searching.symbol = "Three";
            }
            searching = searching.next; 
        }
    }
}
