import java.io.*;
import java.util.Scanner;


// command line must have input and output txt files after compilation. 
public class CustomTree {
    static String addError = "Add operation not possible.";
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

    /** node class */
    public class Node{
        String payLoad;
        Node leftChild;
        Node middleChild; 
        Node rightChild;
        int level; 

        public Node(String payLoad)
        {
            this.payLoad = payLoad;
            this.leftChild = null;
            this.middleChild = null;
            this.rightChild = null; 
        }

        public Node()
        {
            this.payLoad = null;
            this.leftChild = null;
            this.middleChild = null;
            this.rightChild = null;
            this.level =0; 
        }
    }


    /** Making the terneary tree 
     *                        root
     *       lC                mC                 rC
     * lC    mC    rC    lC    mC    rC     lC    mC    rC
     */
    public Node root = new Node("root");

    // making the output file easily accessible for any part of a CustomTree
    public File outputFile = null;

     // this is the powerhouse, handles inputs, sorting and output calls 
     public static void readFile( File input, File out) throws IOException
    {
        CustomTree tree = new CustomTree();
        tree.outputFile = out;  
        tree.root.level = 0; 
        try( Scanner inputScanning = new Scanner(input)){ 
            String[] line = {"" , "", ""};
            boolean dollaSign = false; 
            while (inputScanning.hasNextLine())
            {
                line = breakDown( inputScanning.nextLine() );
                if (line[2].startsWith("$")) 
                { 
                    dollaSign = true; 
                    if (line[2].substring(1).equals(""))
                    {
                        line[0] = "invalid input, exit";
                    }
                }
                else 
                { dollaSign = false; }

                if (line[0].equals("AddL") )
                {
                    tree.addL(line[1], line[2], dollaSign);
                }
                else if (line[0].equals("AddM") )
                {
                    tree.addM(line[1], line[2], dollaSign);
                }
                else if (line[0].equals("AddR") )
                {
                    tree.addR(line[1], line[2], dollaSign);
                }
                else if (line[0].equals("DelL") )
                {
                    tree.delL(line[1]);
                }
                else if (line[0].equals("DelM") )
                {
                    tree.delM(line[1]);
                }
                else if (line[0].equals("DelR") )
                {
                    tree.delR(line[1]);
                }
                else if (line[0].equals("Exchange") )
                {
                    tree.exchange(line[1], line[2], dollaSign);
                }
                else if (line[0].equals("Print"))
                {
                    String[] levels = new String[100]; 
                    levelBuilder(tree.root, tree, levels);
                    tree.print(levels);
                }
                else 
                {
                    output(out,"Input error.");
                    return; 
                }
            }
            inputScanning.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public static String [] breakDown(String line)
    {
        int commaIndexEnd = line.length()-1;
        int commaIndexStart = 0; 
        String [] toReturn = {"", "", ""};
        try {
        toReturn[0] = line.substring(0, line.indexOf("("));
        if (toReturn[0].startsWith("Add") || toReturn[0].startsWith("Exchange"))
        {
           int i = line.indexOf(",");
           while (line.charAt(i) == ',')
           {
               commaIndexEnd = i; 
               commaIndexStart = i; 
               i++;
           }
        }
        if (!line.endsWith(")"))
        {
            toReturn[0] = "another error, will terminate";
        }
        if(line.startsWith("Print") && line.length()!=7)
        {
            toReturn[0] = "more errors, byebye";
        }
        toReturn[1] = line.substring(line.indexOf("(")+1, commaIndexEnd);
        toReturn[2] = line.substring(commaIndexStart+1, line.length()-1);
        } catch (StringIndexOutOfBoundsException e) { /* do nothing, doesnt break anything */ }
        if (  toReturn[1].contains(" ") || toReturn[2].contains(" ") 
            || (toReturn[0].startsWith("Add") || toReturn[0].startsWith("Exchange")) && (toReturn[1].equals("") || toReturn[2].equals("") )
                || (toReturn[0].startsWith("Del") && (toReturn[1].equals("")))        )
        {
            toReturn[0] = "not input, will throw input error in readFile";
        }
        return toReturn; 
    }

    public void addL(String a, String b, boolean dollaSign) throws IOException
    {
        b = (dollaSign)? b.substring(1) : b;
        Node tempNode = new Node();
        Node toUpdate = regSearch(a, this.root, tempNode, false);
        if (toUpdate == null)
        {
            return; 
        }
        if (toUpdate.leftChild == null)
        {
            toUpdate.leftChild = new Node(b); 
            toUpdate.leftChild.level = toUpdate.level+1; 
            return; 
        }
        if (dollaSign)
        {
            toUpdate.leftChild.payLoad = b;
            return;
        }
        output(this.outputFile, addError);
    }

    public void addM(String a, String b, boolean dollaSign) throws IOException
    {
        b = (dollaSign)? b.substring(1) : b;
        Node tempNode = new Node(); 
        Node toUpdate = regSearch(a, this.root, tempNode, false);
        if (toUpdate == null)
        {
            return; 
        }
        if (toUpdate.middleChild == null)
        {
            toUpdate.middleChild = new Node(b); 
            toUpdate.middleChild.level = toUpdate.level+1;
            return; 
        }
        if (dollaSign)
        {
            toUpdate.middleChild.payLoad = b;
            return;
        }
        output(this.outputFile, addError);
    }

    public void addR(String a, String b, boolean dollaSign) throws IOException
    {
        b = (dollaSign)? b.substring(1) : b;
        Node tempNode = new Node();
        Node toUpdate = regSearch(a, this.root, tempNode, false);
        if (toUpdate == null)
        {
            return; 
        }
        if (toUpdate.rightChild == null)
        {
            toUpdate.rightChild = new Node(b); 
            toUpdate.rightChild.level = toUpdate.level+1;
            return; 
        }
        if (dollaSign)
        {
            toUpdate.rightChild.payLoad = b;
            return;
        }
        output(this.outputFile, addError);
    }

    public void delL(String a)
    {
        Node tempNode = new Node();
        Node toDelete = regSearch(a, this.root, tempNode, true);
        if (toDelete == null)
        {
            return; 
        }
        toDelete.leftChild = null; 
    }

    public void delR(String a)
    {
        Node tempNode = new Node();
        Node toDelete = regSearch(a, this.root, tempNode, true);
        if (toDelete == null)
        {
            return; 
        }
        toDelete.rightChild = null; 
    }

    public void delM(String a)
    {
        Node tempNode = new Node(); 
        Node toDelete = regSearch(a, this.root, tempNode, true);
        if (toDelete == null)
        {
            return; 
        }
        toDelete.middleChild = null; 
    }

    public void exchange(String a, String b, boolean dollaSign)
    {
        b = (dollaSign)? b.substring(1) : b;
        exchangeSearch(a, b, this.root, dollaSign);
    }

    public void print(String[] levels) throws IOException
    {
        int i = 0; 
        while ( i < levels.length && levels[i] != null )
        {
            if (levels[i].endsWith(" ; ") && levels[i].startsWith("null"))
            {
                levels[i] = levels[i].substring(4, levels[i].length()-3);
            }
            output (this.outputFile, levels[i]); 
            i++;
        }
    }

    public static void levelBuilder(Node node, CustomTree tree, String[] levels) throws NullPointerException
    {    
        levels[node.level] =  levels[node.level] + node.payLoad + " ; "; 
        if (node.leftChild != null)
            levelBuilder(node.leftChild, tree, levels);
        if (node.middleChild != null)
            levelBuilder(node.middleChild, tree, levels);
        if (node.rightChild != null)
            levelBuilder(node.rightChild, tree, levels);
    }

    public static Node regSearch(String a, Node theNode, Node toReturn, boolean isDel) throws NullPointerException
    {
        toReturn = (theNode.payLoad.equals(a) && theNode.level >= toReturn.level)? theNode : toReturn;
        Node[] kids = kidsSetup(theNode, isDel);
        for (int i = 0; i < 3; i++)
        {
            toReturn = (kids[i] == null)? toReturn : regSearch(a, kids[i], toReturn, isDel);
        }
        return toReturn;
    }

    public static Node[] kidsSetup (Node theNode, boolean isDel)
    {
        if (isDel)
        {   
            Node[] kids =  {theNode.rightChild, theNode.middleChild, theNode.leftChild};
            return kids;
        }
        else 
        {
            Node []kids = {theNode.leftChild, theNode.middleChild, theNode.rightChild};
            return kids;
        }
    }

    public static void exchangeSearch(String a, String b, Node theNode, boolean dollaSign)
    {
        if (theNode.payLoad.equals(a))
        {
            theNode.payLoad = (!dollaSign)? b : theNode.payLoad + b; 
        }
        if (theNode.leftChild !=null)
        {
            exchangeSearch(a, b, theNode.leftChild, dollaSign);
        }
        if (theNode.middleChild !=null)
        {
            exchangeSearch(a, b, theNode.middleChild, dollaSign);
        }
        if (theNode.rightChild !=null)
        {
            exchangeSearch(a, b, theNode.rightChild, dollaSign);
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
}