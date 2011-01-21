package uk.org.taverna.scufl2.api.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import uk.org.taverna.scufl2.api.common.AbstractNamedChild;
import uk.org.taverna.scufl2.api.common.Child;
import uk.org.taverna.scufl2.api.common.NamedSet;
import uk.org.taverna.scufl2.api.common.Ported;
import uk.org.taverna.scufl2.api.common.Visitor;
import uk.org.taverna.scufl2.api.common.WorkflowBean;
import uk.org.taverna.scufl2.api.dispatchstack.DispatchStack;
import uk.org.taverna.scufl2.api.iterationstrategy.IterationStrategyStack;
import uk.org.taverna.scufl2.api.port.InputProcessorPort;
import uk.org.taverna.scufl2.api.port.OutputProcessorPort;


/**
 * @author Alan R Williams
 *
 */
public class Processor extends AbstractNamedChild implements Child<Workflow>,
		Ported {

	private NamedSet<OutputProcessorPort> outputPorts = new NamedSet<OutputProcessorPort>();
	private NamedSet<InputProcessorPort> inputPorts = new NamedSet<InputProcessorPort>();
	private IterationStrategyStack iterationStrategyStack = new IterationStrategyStack(
			this);
	private DispatchStack dispatchStack = new DispatchStack(this);
	private Workflow parent;

	public Processor() {
		super();
	}

	/**
	 * @param parent
	 * @param name
	 */
	public Processor(Workflow parent, String name) {
		super(name);
		setParent(parent);
	}

	@Override
	public boolean accept(Visitor visitor) {
		if (visitor.visitEnter(this)) {
			List<Iterable<? extends WorkflowBean>> children = new ArrayList<Iterable<? extends WorkflowBean>>();
			children.add(getInputPorts());
			children.add(getOutputPorts());
			for (Iterable<? extends WorkflowBean> it : children) {
				for (WorkflowBean bean : it) {
					if (!bean.accept(visitor)) {
						break;
					}
				}
			}
			getIterationStrategyStack().accept(visitor);
			getDispatchStack().accept(visitor);
		}
		return visitor.visitLeave(this);
	}

	public DispatchStack getDispatchStack() {
		return dispatchStack;
	}

	public NamedSet<InputProcessorPort> getInputPorts() {
		return inputPorts;
	}

	public IterationStrategyStack getIterationStrategyStack() {
		return iterationStrategyStack;
	}

	public NamedSet<OutputProcessorPort> getOutputPorts() {
		return outputPorts;
	}

	public Workflow getParent() {
		return parent;
	}

	public void setDispatchStack(DispatchStack dispatchStack) {
		this.dispatchStack = dispatchStack;
	}

	public void setInputPorts(Set<InputProcessorPort> inputPorts) {
		this.inputPorts.clear();
		this.inputPorts.addAll(inputPorts);
	}

	public void setIterationStrategyStack(
			IterationStrategyStack iterationStrategyStack) {
		this.iterationStrategyStack = iterationStrategyStack;
	}

	public void setOutputPorts(Set<OutputProcessorPort> outputPorts) {
		this.outputPorts.clear();
		this.outputPorts.addAll(outputPorts);
	}

	public void setParent(Workflow parent) {
		if (this.parent != null && this.parent != parent) {
			this.parent.getProcessors().remove(this);
		}
		this.parent = parent;
		if (parent != null) {
			parent.getProcessors().add(this);
		}
	}

	@Override
	public String toString() {
		return super.toString() + "[" + "getInputPorts()=" + getInputPorts()
				+ ", " + "getOutputPorts()=" + getOutputPorts() + ", " + "]";
	}

}

