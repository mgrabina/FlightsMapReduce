package ar.edu.itba.pod.server.serviceClients;

import ar.edu.itba.pod.constants.Constants;
import ar.edu.itba.pod.constants.ElectionsState;
import ar.edu.itba.pod.models.Vote;
import ar.edu.itba.pod.services.AdministrationService;
import ar.edu.itba.pod.services.InspectionService;
import ar.edu.itba.pod.services.VotingService;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VotingServiceImpl extends UnicastRemoteObject implements VotingService {

    private List<Vote> votes;
    private InspectionService inspectionService;
    private AdministrationService administrationService;
    
    public VotingServiceImpl() throws RemoteException, MalformedURLException, NotBoundException {
        super();

        this.votes = new ArrayList<>();
        this.inspectionService = (InspectionService) Naming.lookup(Constants.inspectionServiceHost + "inspectionService");
        this.administrationService = (AdministrationService) Naming.lookup(Constants.administrationServiceHost + "administrationService");
    }

    @Override
    public void vote(Vote vote) throws RemoteException {
        if (administrationService.getElectionsState() != ElectionsState.RUNNING){
            throw new IllegalStateException("There aren't elections running.");
        }
        this.votes.add(vote);
        inspectionService.alertInspector(vote);
    }

    @Override
    public List<Vote> getVotes() throws RemoteException {
        return votes;
    }


}
