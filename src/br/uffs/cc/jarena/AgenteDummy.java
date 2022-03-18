/**
 * Estudante: Pedro Zawadzki Dutra
 */

package br.uffs.cc.jarena;

public class AgenteDummy extends Agente {
	static int contador = 0;

	private int agentId;

	private int distanciaGrupo;
	private int[] msgs;
	private int init;

	public AgenteDummy(Integer x, Integer y, Integer energia) {
		super(Constants.LARGURA_MAPA / 2, Constants.ALTURA_MAPA / 2, energia);
		agentId = contador++;
		distanciaGrupo = 4;

		msgs = new int[] { 0, 0, 0, 0 };

		init = distanciaGrupo;
	}

	public void pensa() {
		System.out.println(toString());

		if (init-- > 0) {
			setDirecao(getFuncao());
			return;
		} else if (init-- == -1) {
			setDirecao(getGrupo() + 1);
			return;
		}

		if (bateuNaMargem(getDirecao())) {
			setDirecao(inverteDirecao(getDirecao()));
			return;
		}

		int soma = msgs[0] + msgs[1] + msgs[2] + msgs[3];
		if (soma == 4) {
			para();
		} else if (soma == 3) {
			setDirecao(inverteDirecao(indexOf(msgs, 0) + 1));
		} else if (soma == 1 || soma == 2) {
			setDirecao(indexOf(msgs, 1) + 1);
		} else if (soma == 0 && isParado()) {
			setDirecao(getGrupo());
		}

		msgs = new int[] { 0, 0, 0, 0 };
	}

	public void recebeuEnergia() {
		enviaMensagem("ENERGIA " + getGrupo() + " " + getFuncao());
	}

	public void tomouDano(int energiaRestanteInimigo) {
	}

	public void ganhouCombate() {
	}

	public void recebeuMensagem(String msg) {
		String[] decode = msg.split(" ");
		String msgType = decode[0];

		if (msgType.equals("ENERGIA")) {
			int senderGrupo = Integer.parseInt(decode[1]);

			if (senderGrupo == getGrupo()) {
				int senderFunc = Integer.parseInt(decode[2]);
				msgs[senderFunc - 1] = 1;
			}
		}
	}

	public String getEquipe() {
		return "fedido";
	}

	private int getFuncao() {
		return (agentId % 4) + 1;
	}

	private int getGrupo() {
		return agentId / 4;
	}

	private int inverteDirecao(int direcao) {
		return direcao + (direcao % 2 == 0 ? -1 : 1);
	}

	public int indexOf(int[] array, int value) {
		for (int i = 0; i < array.length; i++)
			if (array[i] == value)
				return i;
		return -1;
	}

	public boolean bateuNaMargem(int direcao) {
		int offset = distanciaGrupo * 5;

		if (!podeMoverPara(direcao)) {
			return true;
		}

		if (direcao == getFuncao()) {
			offset = offset * 0;
		} else if (getFuncao() == inverteDirecao(direcao)) {
			offset = offset * 2;
		} else {
			offset = offset * 1;
		}

		switch (direcao) {
			case DIREITA:
				return getX() + offset >= Constants.LARGURA_MAPA;
			case ESQUERDA:
				return getX() - offset <= 0;
			case CIMA:
				return getY() - offset <= 0;
			case BAIXO:
				return getY() + offset >= Constants.ALTURA_MAPA;
			default:
				return false;
		}
	}
}