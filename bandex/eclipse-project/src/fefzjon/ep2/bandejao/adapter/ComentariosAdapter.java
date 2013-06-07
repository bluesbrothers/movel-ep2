package fefzjon.ep2.bandejao.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import fefzjon.ep2.bandejao.R;
import fefzjon.ep2.bandejao.utils.BandexComment;

public class ComentariosAdapter extends BaseAdapter {
	private Context context;
	private List<BandexComment> listaComentarios;

	public ComentariosAdapter(final Context context,
			final List<BandexComment> listaComentarios) {
		this.context = context;
		this.listaComentarios = listaComentarios;
	}

	@Override
	public int getCount() {
		return this.listaComentarios.size();
	}

	@Override
	public Object getItem(final int i) {
		return this.listaComentarios.get(i);
	}

	@Override
	public long getItemId(final int i) {
		return i;
	}

	@Override
	public View getView(final int i, final View view, final ViewGroup viewGroup) {
		BandexComment comment = this.listaComentarios.get(i);

		LayoutInflater inflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View aView = inflater.inflate(R.layout.view_comentario_item, null);

		TextView commenterView = (TextView) aView
				.findViewById(R.id.item_comentario_commenter);
		TextView messageView = (TextView) aView
				.findViewById(R.id.item_comentario_message);

		commenterView.setText(comment.getCommenter());
		messageView.setText(comment.getMessage());

		return aView;
	}

}
