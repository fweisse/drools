package org.drools.command.runtime.rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import org.drools.command.Context;
import org.drools.command.impl.GenericCommand;
import org.drools.command.impl.KnowledgeCommandContext;
import org.drools.impl.StatefulKnowledgeSessionImpl;
import org.drools.reteoo.ReteooWorkingMemory;

import org.drools.result.ExecutionResults;
import org.drools.result.QueryResult;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.FactHandle;
import org.drools.runtime.rule.QueryResults;
import org.drools.runtime.rule.impl.NativeQueryResults;

@XmlAccessorType( XmlAccessType.NONE )
public class QueryCommand  implements GenericCommand<QueryResults> {

    @XmlAttribute(name = "out-identifier")
    private String outIdentifier;
    @XmlAttribute(required = true)
    private String name;
    @XmlElement(name="argument")
    private List<Object> arguments;

    public QueryCommand() {
    }
    
    public QueryCommand(String outIdentifier, String name, Object[] arguments) {
        this.outIdentifier = outIdentifier;
        this.name = name;
        this.arguments = arguments != null ? Arrays.asList( arguments ) : Arrays.asList();
    }
    
    public String getOutIdentifier() {
        return outIdentifier;
    }

    public void setOutIdentifier(String outIdentifier) {
        this.outIdentifier = outIdentifier;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<Object> getArguments() {
        if (this.arguments == null) {
            this.arguments = new ArrayList<Object>();
        }
        return this.arguments;
    }
    public void setArguments(List<Object> arguments) {
        this.arguments = arguments;
    }

    public QueryResults execute(Context context) {
        StatefulKnowledgeSession ksession = ((KnowledgeCommandContext) context).getStatefulKnowledgesession();
        
        QueryResults results = null;
        
        if ( this.arguments == null || this.arguments.size() == 0 ) {
            results = ksession.getQueryResults( name );
        } else {
            results = ksession.getQueryResults( name, this.arguments.toArray() );
        }
        
        if ( this.outIdentifier != null ) {
            ExecutionResults execRes =
                (ExecutionResults)((StatefulKnowledgeSessionImpl) ksession).session.getExecutionResult();
            QueryResult queryRes = new QueryResult( outIdentifier, results );
            execRes.getResults().add( queryRes );
        }

        return results;
    }
}