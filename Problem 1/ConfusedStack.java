import java.io.*;
import java.nio.file.Files;
import java.util.Scanner;


// command line must have input and output txt files after compilation. 
public class ConfusedStack {
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

        // begin to read the file and conduct the confused stack. 
        readFile(in, out);
        // tidy up output file, remove last line
        try
        { 
            StringBuilder sb = new StringBuilder();
            try (Scanner sc = new Scanner(out)) {
                String currentLine;
                while(sc.hasNext() ) {
                    if (sb.isEmpty()){ } 
                    else { sb.append("\n"); }
                    currentLine = sc.nextLine();
                    sb.append(currentLine);
                    
                }
            }
            //Delete File Content
            PrintWriter pw = new PrintWriter(out);
            pw.close();
            BufferedWriter writer = new BufferedWriter(new FileWriter(out, true));
            writer.append(sb.toString());
            writer.flush();
            writer.close();
            



            /*
            long length = output.length()-1;
            do {                     
            output.seek(length);
            last = output.readByte();
            length -= 1;
            } while(last != 10);
            output.setLength(length);  
            */
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * readFile takes lines from the input file and turns them into commands for testing
     * @param input
     * @param out
     * @throws IOException
     */

    public static void readFile( File input, File out) throws IOException
    {
        try( Scanner inputScanning = new Scanner(input)){ 
            ConfusedStack myStack = new ConfusedStack();
            myStack.outputFile = out; 
            String line = null;
            boolean cont = true; 
            while (inputScanning.hasNextLine() && cont )
            {
                line = inputScanning.nextLine();
                cont = readCommand(line, myStack);
            }
            inputScanning.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * readCommand takes the String and trims, then tests for propper stack commands
     * Uses myStack to begin to use the commands
     * @param line
     * @param myStack
     * @throws IOException
     */
    public static boolean readCommand(String line, ConfusedStack myStack) throws IOException
    {
        if (line.startsWith("push(") && line.endsWith(")"))     // has push("some chars")
        {
            line = line.substring(5, line.length()-1);
            if (line.matches("[0-9]+" )== false || ( line.startsWith("0") && line.length() > 1))               // integers only 
            {
                output(myStack.outputFile, "Imput error.");
                return false; 
            }
            myStack.push(Integer.parseInt(line));               //push if true 
            return true; 
        }
        else if (line.equals("pop()"))                          // contains only pop()
        {
            if (myStack.listSize == 0)
            {
                output(myStack.outputFile, "Error");            // if myStack empty, output "Error"
                return false; 
            }
            else 
            {
                myStack.pop();                                 // pop(), otherwise 
                return true; 
            }
        }
        else if (line.equals("top()"))                          // contains only top()
        {   
            if (myStack.listSize == 0)
            {
                output(myStack.outputFile, "null");             // if myStack empty, output "null"
                return true; 
            }
            else 
            {   
                myStack.top();                                  // top(), otherwise
                return true; 
            }
        }
        else 
        {
            output(myStack.outputFile, "Input error.");          // if the command is none of the above, output "Input Error"
            return false; 
        }
    }
    
    /**
     * output method
     * prints the number arg to the output file 
     * if number == null, print string "null"
     * @param number
     * returns nothing
     */
    public static void output(File output, String number) throws IOException 
    {
        FileWriter fw = new FileWriter(output, true);
        try( BufferedWriter writer = new BufferedWriter(fw)){
            writer.write(number);
            writer.newLine();
            writer.flush();
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    

    /**
     * Making the singly linked list for a stack, will be used as a ConfusedStack object
     * each node consists of integer data and a next node. 
     */
    public class Node{
        int data;
        Node next; 

        public Node(int data)
        {
            this.data = data;
            this.next = null;
        }

    }

    
        // initializing the tip of the list
        Node tip = null;     
        int listSize = 0;  
        
        // making the output file easily accessible for any part of a ConfusedStack
        public File outputFile = null;


        /**
         * remove method 
         * removes the node argument given by
         * setting the previous node.next = null so that the next search will end 
         * returns nothing
         */
        public void remove ()
        {
            if (listSize != 0)
            {
                tip = tip.next; 
                listSize --;
            }
        }
        /**
         * insert method 
         * adds the data to a new node at the front (top) of the list
         * @param number
         */
        public void insert(int number)
        {
            Node current = new Node (number);
            current.next = tip; 
            tip = current; 
            listSize++;
        }


        /**
         * adds the argument as a new node data to the end of the list if certain
         * conditions occur, will become a confused stack
         * 
         * @param number
         * @throws IOException
         */
        public void push(int number) throws IOException
        {
            // confused behaviour of push(number)
            if ( number == 0 && tip == null ) // no fix available
            {
                tip = new Node(0);
                listSize++; 
            }
            else if (number == 666)
            {
                for (int i=0; i < 3; i++)
                {
                    insert(number);
                }
            }
            else if (number == 3)
            {
                insert(7);
            }
            else if (number == 13)
            {
                while (listSize > 1) 
                {
                    output(outputFile, Integer.toString(tip.data));
                    remove(); 
                }
                output(outputFile, Integer.toString(tip.data));
                tip.data = 13;
            }

            // usual behaviour of push()
            else
            {
                insert(number);
            }
        }
        
        /**
         * returns the element on the top of the list, called by "list.pop()" then
         * removes that element under certain conditions, the confused stack will act
         * confused
         * 
         * @throws IOException
         */
        public void pop() throws IOException
        {
            //confused behaviour of pop()
            if (tip.data == 666)
            {
                output(outputFile, Integer.toString(tip.data));
                remove(); 
                remove();
            }
            else if( tip.data == 7)
            {
                output(outputFile, Integer.toString(tip.data));
            }
            else if ( tip.data == 42)
            {
                while (tip.next != null)
                {
                    remove();
                }
                remove();
                output (outputFile, "42");
            }

            // usual behaviour of pop()
            else 
            {
                output(outputFile, Integer.toString(tip.data));
                remove();
            }
        }

        /**
         * top outputs the last item on the stack but does not remove anything
         * confused top behaviour may remove or not output certain values
         * @throws IOException
         */
        public void top() throws IOException
        {
            // confused behaviour of top()
            if (tip.data == 666)
            {
                output(outputFile, "999");
            }
            else if( tip.data == 319)
            {
                output(outputFile, "666");
            }
            else if(tip.data == 7)
            {
                remove();
            }

            // usual behaviour of top()
            else
            {
                output(outputFile, Integer.toString(tip.data));
            }
        }
}