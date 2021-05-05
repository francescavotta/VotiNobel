package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {
	private List<Esame> partenza;
	private Set<Esame> soluzioneMigliore;
	private double mediaSoluzioneMigliore;

	public Model() {
		EsameDAO dao = new EsameDAO();
		this.partenza = dao.getTuttiEsami();
	}
	
	public Set<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		Set<Esame> parziale = new HashSet<Esame>();
		this.soluzioneMigliore = new HashSet<Esame>();
		mediaSoluzioneMigliore = 0;
		
		cerca2(parziale, 0, numeroCrediti);
		
		return soluzioneMigliore;	
	}
	//complessità 2^N, meno del fattoriale ma è comunque esponenziale
	private void cerca2(Set<Esame> parziale, int L, int m) {
		// casi terminali

		//ho già raggiunto la somma dei crediti
		int crediti = sommaCrediti(parziale);

		if(crediti>m) {
			return;
		}
		if(crediti==m) {
			double media = calcolaMedia(parziale);
			if(media > mediaSoluzioneMigliore) {
				soluzioneMigliore = new HashSet<Esame>(parziale);
				mediaSoluzioneMigliore = media;
			}

			return;
		}
		//sicuramente crediti < m
		//raggiungo la fine dei livelli, non ho più esami
		if(L == partenza.size()) {
			return;
		}
		
		//genero i sottoproblemi
		//partenza[L] è da aggiungere?
		//provo un ramo e lascio avanti la ricorsione
		parziale.add(partenza.get(L));
		cerca2(parziale, L+1, m);
		
		//provo il secondo ramo
		parziale.remove(partenza.get(L));
		cerca2(parziale, L+1,m);
		
	}
	//complessità fattoriale!(N!) con 50 crediti si blocca
	private void cerca1(Set<Esame> parziale, int L, int m) {
		// casi terminali
		
		//ho già raggiunto la somma dei crediti
		int crediti = sommaCrediti(parziale);
		
		if(crediti>m) {
			return;
		}
		if(crediti==m) {
			double media = calcolaMedia(parziale);
			if(media > mediaSoluzioneMigliore) {
				soluzioneMigliore = new HashSet<Esame>(parziale);
				mediaSoluzioneMigliore = media;
			}
			
			return;
		}
		//sicuramente crediti < m
		//raggiungo la fine dei livelli, non ho più esami
		if(L == partenza.size()) {
			return;
		}
		
		//generare i sotto-problemi
		for(Esame e: partenza) {
			if(!parziale.contains(e)) {
				//TODO EVITARE DI CONTROLLARE SOLUZIONI INUTILI
				parziale.add(e);
				cerca1(parziale, L+1, m);
				parziale.remove(e);//backtracking
			}
		}	
	}

	public double calcolaMedia(Set<Esame> esami) {
		
		int crediti = 0;
		int somma = 0;
		
		for(Esame e : esami){
			crediti += e.getCrediti();
			somma += (e.getVoto() * e.getCrediti());
		}
		
		return somma/crediti;
	}
	
	public int sommaCrediti(Set<Esame> esami) {
		int somma = 0;
		
		for(Esame e : esami)
			somma += e.getCrediti();
		
		return somma;
	}

}
