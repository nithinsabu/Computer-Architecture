package generic;

import java.io.DataOutputStream;
import java.io.FileInputStream;

import generic.Instruction.OperationType;
import generic.Operand.OperandType;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.management.openmbean.OpenMBeanConstructorInfo;
public class Simulator {
		
	static FileInputStream inputcodeStream = null;
	public static Map<Instruction.OperationType, String> opcode = new HashMap<Instruction.OperationType, String>() {{
		put(Instruction.OperationType.add, "00000");
		put(Instruction.OperationType.addi, "00001");
		put(Instruction.OperationType.sub, "00010");
		put(Instruction.OperationType.subi, "00011");
		put(Instruction.OperationType.mul, "00100");
		put(Instruction.OperationType.muli, "00101");
		put(Instruction.OperationType.div, "00110");
		put(Instruction.OperationType.divi, "00111");
		put(Instruction.OperationType.and, "01000");
		put(Instruction.OperationType.andi, "01001");
		put(Instruction.OperationType.or, "01010");
		put(Instruction.OperationType.ori, "01011");
		put(Instruction.OperationType.xor, "01100");
		put(Instruction.OperationType.xori, "01101");
		put(Instruction.OperationType.slt, "01110");
		put(Instruction.OperationType.slti, "01111");
		put(Instruction.OperationType.sll, "10000");
		put(Instruction.OperationType.slli, "10001");
		put(Instruction.OperationType.srl, "10010");
		put(Instruction.OperationType.srli, "10011");
		put(Instruction.OperationType.sra, "10100");
		put(Instruction.OperationType.srai, "10101");
		put(Instruction.OperationType.load, "10110");
		put(Instruction.OperationType.end, "11101");
		put(Instruction.OperationType.beq, "11001");
		put(Instruction.OperationType.jmp, "11000");
		put(Instruction.OperationType.bne, "11010");
		put(Instruction.OperationType.blt, "11011");
		put(Instruction.OperationType.bgt, "11100");
	}};

	

	public static void setupSimulation(String assemblyProgramFile)
	{	
		int firstCodeAddress = ParsedProgram.parseDataSection(assemblyProgramFile);
		System.out.println(firstCodeAddress);
		ParsedProgram.parseCodeSection(assemblyProgramFile, firstCodeAddress);
		ParsedProgram.printState();
	}
	
	public static void assemble(String objectProgramFile)
	{	

		
		try{
            FileOutputStream objectfile = new FileOutputStream(objectProgramFile);
			DataOutputStream outputfile = new DataOutputStream(objectfile);
			//writing firstcodeaddress
			outputfile.writeInt(ParsedProgram.firstCodeAddress);
			//writing data
			for (Integer a: ParsedProgram.data){
				outputfile.writeInt(a.intValue());
			}
			//writing instructions
			for (Instruction a: ParsedProgram.code){
				String b = opcode.get(a.getOperationType());

				switch(a.getOperationType())
				{
					case add : 
					case sub : 
					case mul : 
					case div : 
					case and : 
					case or : 
					case xor : 
					case slt : 
					case sll : 
					case srl : 
					case sra :	{
									b+=String.format("%5s", Integer.toBinaryString(a.getSourceOperand1().value)).replace(' ','0');
									b+=String.format("%5s", Integer.toBinaryString(a.getSourceOperand2().value)).replace(' ','0');
									b+=String.format("%5s", Integer.toBinaryString(a.getDestinationOperand().value)).replace(' ','0');
									b+="0".repeat(12);
									int outvalue;
									if (b.charAt(0)=='0'){
										outvalue= Integer.parseInt(b,2);
									}
									else{
										String onecmp= "";
										for (int g=1; g<b.length();g++){
											char c = b.charAt(g);
											if (c=='0'){onecmp+='1';}
											else{onecmp+='0';}
										}
										outvalue= Integer.parseInt(onecmp,2)+1;
										outvalue = -outvalue;
									}
									outputfile.writeInt(outvalue);
									break;
								} 
					
					//R2I type
					case addi :
					case subi :
					case muli :
					case divi : 
					case andi : 
					case ori : 
					case xori : 
					case slti : 
					case slli : 
					case srli : 
					case srai :
					case load :
					case store :	{
						b+=String.format("%5s", Integer.toBinaryString(a.getSourceOperand1().value)).replace(' ','0');
						b+=String.format("%5s", Integer.toBinaryString(a.getDestinationOperand().value)).replace(' ','0');
						if (a.getOperationType()==OperationType.load || a.getOperationType()==OperationType.store){
							b+=String.format("%17s", Integer.toBinaryString(ParsedProgram.symtab.get(a.getSourceOperand2().labelValue))).replace(' ','0');
						}else{
							b+=String.format("%17s", Integer.toBinaryString(a.getSourceOperand2().value)).replace(' ','0');
						}
						int outvalue;
						if (b.charAt(0)=='0'){
							outvalue= Integer.parseInt(b,2);
						}
						else{
							String onecmp= "";
							for (int g=1; g<b.length();g++){
								char c = b.charAt(g);
								if (c=='0'){onecmp+='1';}
								else{onecmp+='0';}
							}
							outvalue= Integer.parseInt(onecmp,2)+1;
							outvalue = -outvalue;
						}
						outputfile.writeInt(outvalue);
						break;
									} 
					
					case beq : 
					case bne : 
					case blt : 
					case bgt : 	{
						b+=String.format("%5s", Integer.toBinaryString(a.getSourceOperand1().value)).replace(' ','0');
						b+=String.format("%5s", Integer.toBinaryString(a.getSourceOperand2().value)).replace(' ','0');
						String imm= Integer.toBinaryString(ParsedProgram.symtab.get(a.getDestinationOperand().labelValue)-a.getProgramCounter());
						if (imm.length()>17){
							imm=imm.substring(imm.length()-17);
						}
						b+=String.format("%17s", imm).replace(' ','0');int outvalue;
						if (b.charAt(0)=='0'){
							outvalue= Integer.parseInt(b,2);
						}
						else{
							String onecmp= "";
							for (int g=1; g<b.length();g++){
								char c = b.charAt(g);
								if (c=='0'){onecmp+='1';}
								else{onecmp+='0';}
							}
							outvalue= Integer.parseInt(onecmp,2)+1;
							outvalue = -outvalue;
						}
						outputfile.writeInt(outvalue);
						break;
								}
					
					//RI type :
					case jmp :		
						{
						b+="0".repeat(5);
						String imm= Integer.toBinaryString(ParsedProgram.symtab.get(a.getDestinationOperand().labelValue)-a.getProgramCounter());
						if (imm.length()>22){
							imm=imm.substring(imm.length()-22);
						}
						b+=String.format("%22s", imm).replace(' ','0');
						int outvalue;
						if (b.charAt(0)=='0'){
							outvalue= Integer.parseInt(b,2);
						}
						else{
							String onecmp= "";
							for (int g=1; g<b.length();g++){
								char c = b.charAt(g);
								if (c=='0'){onecmp+='1';}
								else{onecmp+='0';}
							}
							outvalue= Integer.parseInt(onecmp,2)+1;
							outvalue = -outvalue;
						}
						outputfile.writeInt(outvalue);	
						break;
						}
		
					case end :{
						b+="0".repeat(27);
						int outvalue;
						if (b.charAt(0)=='0'){
							outvalue= Integer.parseInt(b,2);
						}
						else{
							String onecmp= "";
							for (int g=1; g<b.length();g++){
								char c = b.charAt(g);
								if (c=='0'){onecmp+='1';}
								else{onecmp+='0';}
							}
							outvalue= Integer.parseInt(onecmp,2)+1;
							outvalue = -outvalue;
						}
						outputfile.writeInt(outvalue);
						break;
					}	
						
					default: Misc.printErrorAndExit("unknown instruction!!");
				}
			}

			outputfile.close();
			}
			catch(IOException e){
				e.printStackTrace();
			}
			//TODO your assembler code
		//1. open the objectProgramFile in binary mode
		//2. write the firstCodeAddress to the file
		//3. write the data to the file
		//4. assemble one instruction at a time, and write to the file
		//5. close the file
	}
	
}
