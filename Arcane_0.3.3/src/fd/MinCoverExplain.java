package fd;

import java.util.ArrayList;
import java.util.List;

public class MinCoverExplain {

	String log;
	FDSet output;
	
	public MinCoverExplain(FDSet input){
		log = "";
		this.output = null;
		log += "Step 1: Ensure that F has singlton RHS:\n";
		output = explainReduceSingltonRHS(input);
		log += "Now F = "+output + "\n\n";
		log += "Step 2: Ensure that F has no extraneous LHS:\n";
		output = explainReduceNoExtraLHS(output);
		log += "Now F = "+output +"\n\n";
		log += "Step 3: Ensure that F has no redundant FDs:\n";
		output = explainReduceNoRedundantFDs(output);
		log += "Now F = "+output+"\nwhich is the minimal cover.\n";
	}	

	// -----------------------------------------------------------------
	
	public String getLog() {
		return log;
	}

	public FDSet getOutput() {
		return output;
	}

	public FDSet explainReduceNoRedundantFDs(FDSet f){
		FDSet e = new FDSet(f);
		for(FD fd:f){
			if(FDUtility.isRedundantFD(fd, e)){
				e.remove(fd);
				log += fd+" is redundant, so it is eliminated\n";
			}
		}
		return e;
	}
	

	public FD exReduceLHS(FD fd, FDSet f){		
		AttributeSet lhs = fd.getLHS();
		if(lhs.size()>1){
			List<Attribute> llhs = new ArrayList<Attribute>(lhs);
			try {
				int i = 0;
				while(i<llhs.size() ){
					Attribute a = llhs.get(i);					
					if(FDUtility.isExtraLHS(a, fd, f)){
						AttributeSet lhs_a = new AttributeSet(lhs);
						lhs_a.remove(a);
						FD fd_ = new FD(lhs_a, fd.getRHS());
						log += "Attribute "+a.getName()+" is extraneous in LHS of "+fd+", so it is removed and we get :"+fd_+"\n";
						//return reduceLHS(fd_, f);
						return fd_;
					} else {
						i++;
					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}	
			return fd;
		} else {
			return fd;
		}
	}
	
	
	public FDSet explainReduceNoExtraLHS(FDSet f){
		FDSet e = new FDSet();
		for(FD fd:f){
			AttributeSet lhs = fd.getLHS();
			if(lhs.size()>1){
				FD fd_ = exReduceLHS(fd, f);
				while(!fd.equals(fd_)){
					fd = fd_;
					fd_ = exReduceLHS(fd, f);					
				}
				e.add(fd_);				
			} else {
				e.add(fd);
			}
		}
		return e;
	}
	
	public FDSet explainReduceSingltonRHS(FDSet f){
		FDSet e = new FDSet();
		for(FD fd:f){
			AttributeSet rhs = fd.getRHS();
			if(rhs.size()>1){
				AttributeSet lhs = fd.getLHS();
				log += fd+" is replaced with : \n";
				for(Attribute x:rhs){
					AttributeSet x_ = new AttributeSet(); 	x_.add(x);
					FD fd_ = new FD(lhs, x_);
					if(!fd_.isTrivial()){
						e.add(fd_);
						log += "\t"+fd_+"\n";
					}					
				}
				log += "\n";
			} else {
				e.add(fd);
			}
		}
		return e;
	}
}
