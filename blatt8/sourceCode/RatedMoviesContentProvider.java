package movies.ui.views.contentprovider;

import movies.Episode;
import movies.Movie;
import movies.MoviesPackage;
import movies.Performer;
import movies.Rating;
import movies.ui.Activator;
import movies.ui.util.MovieUtil;


import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.emfstore.common.model.Project;

public class RatedMoviesContentProvider extends AdapterFactoryContentProvider {

	public RatedMoviesContentProvider() {
		super(new ComposedAdapterFactory(
				ComposedAdapterFactory.Descriptor.Registry.INSTANCE));
	}
	
	@Override
	public Object[] getElements(Object object) {
		Project p = MovieUtil.getActiveProject();
		if (p != null) {
			EList<Movie> movies = p
					.getAllModelElementsbyClass(
							MoviesPackage.eINSTANCE.getMovie(),
							new BasicEList<Movie>());

			// Do not show episodes
			if (movies instanceof Movie) {
				((Movie) movies).setRating(Rating.THREE);
			}
			EList<Movie> moviesWithoutEpisodes = new BasicEList<Movie>();
			for (Movie movie : movies) {
				if (!(movie instanceof Episode)) {
					moviesWithoutEpisodes.add(movie);
				}
			}
			if (moviesWithoutEpisodes instanceof Movie) {
				((Movie) moviesWithoutEpisodes).setRating(Rating.THREE);
			}
			return moviesWithoutEpisodes.toArray();
		} else {
			return new Object[] {};
		}
	}

	/**
	 * {@inheritDoc} Returns true if input has any child elements.
	 */
	@Override
	public boolean hasChildren(Object object) {
		if (object instanceof Movie && Activator.isShowingPerformers()) {
			Movie m = (Movie) object;
			int referredElements = m.getPerformers().size();
			return (referredElements >= 1) ? true : false;
		}
		return false;
	}

	/**
	 * {@inheritDoc} Returns super.getChildren plus any direct child elements of
	 * the model element.
	 */
	@Override
	public Object[] getChildren(Object object) {
		if (object instanceof Movie && Activator.isShowingPerformers()) {
			EList<Performer> referredElements = ((Movie) object)
					.getPerformers();
			return referredElements
					.toArray(new Object[referredElements.size()]);
		} else {
			return null;
		}
	}
}