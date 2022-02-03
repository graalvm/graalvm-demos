__title__ = 'giphypop'
__version__ = '0.2'
__author__ = 'Shaun Duncan'
__license__ = 'MIT'
__copyright__ = 'Copyright 2013 Shaun Duncan'


import warnings
import requests

from functools import partial


GIPHY_API_ENDPOINT = 'http://api.giphy.com/v1/gifs'
GIPHY_UPLOAD_ENDPOINT = 'http://upload.giphy.com/v1/gifs'

# Note this is a public beta key and may be inactive at some point
GIPHY_PUBLIC_KEY = 'dc6zaTOxFJmzC'


DEFAULT_SEARCH_LIMIT = 25


class GiphyApiException(Exception):
    pass


class AttrDict(dict):

    """
    A subclass of dict that exposes keys as attributes
    """

    def __getattr__(self, attr):
        if attr in self.__dict__:
            return self.__dict__[attr]

        try:
            return self[attr]
        except KeyError:
            raise AttributeError("'%s' object has no attribute '%s'" %
                                 (self.__class__.__name__, attr))

    def __setattr__(self, attr, value):
        if attr in self.__dict__:
            self.__dict__[attr] = value
        else:
            self[attr] = value


class GiphyImage(AttrDict):

    """
    A special case AttrDict that handles data specifically being returned
    from the giphy api (i.e. integer values converted from strings). The structure
    is very object-like, but retains all the qualities of a python dict.
    Attributes are not a direct mirror of giphy api results, but follow this pattern::

        <Result Object>
            - id
            - type: image type (i.e. gif)
            - url: URL to giphy page
            - raw_data: copy of original data response from giphy (JSON)
            - fullscreen: bit.ly link to giphy fullscreen gif
            - tiled: bit.ly link to giphy tiled gif
            - bitly: bit.ly version of `url`
            - media_url: URL directly to image (original size)
            - frames: number of frames
            - height: image height (original image)
            - width: image width (original image)
            - size: filesize (in bytes, original image)
            - fixed_height: (variable width @ 200px height)
                - url: URL directly to image
                - width: image width
                - height: image height
                - downsampled:
                    - url: URL directly to image
                    - width: image width
                    - height: image height
                - still: (a still image of gif)
                    - url: URL directly to image
                    - width: image width
                    - height: image height
            - fixed_width: (variable height @ 200px width)
                - url: URL directly to image
                - width: image width
                - height: image height
                - downsampled:
                    - url: URL directly to image
                    - width: image width
                    - height: image height
                - still: (a still image of gif)
                    - url: URL directly to image
                    - width: image width
                    - height: image height
    """

    def __init__(self, data=None):
        if data:
            super(GiphyImage, self).__init__(id=data.get('id'),
                                             url=data.get('url'),
                                             type=data.get('type'),
                                             raw_data=data)

            # bit.ly urls
            self.fullscreen = data.get('bitly_fullscreen_url')
            self.tiled = data.get('bitly_tiled_url')
            self.bitly = data.get('bitly_gif_url')

            # Shorthand
            self._make_images(data.get('images', {}))

    def __repr__(self):
        return '%s<%s> at %s' % (self.__class__.__name__, self.id, self.url)

    def __str__(self):
        return self.url

    __unicode__ = __str__

    def open(self):
        """
        Opens the giphy url in a web browser
        """
        # Imported here because this method is MAGIC
        import webbrowser
        webbrowser.open(self.url)

    @property
    def media_url(self):
        """
        The media URL of the gif at its original size
        """
        return self.original.url

    @property
    def frames(self):
        """
        The number of frames of the gif
        """
        return self.original.frames

    @property
    def width(self):
        """
        The width of the gif at its original size
        """
        return self.original.width

    @property
    def height(self):
        """
        The height of the gif at its original size
        """
        return self.original.height

    @property
    def filesize(self):
        """
        The size of the original size file in bytes
        """
        return self.original.size

    def _make_images(self, images):
        """
        Takes an image dict from the giphy api and converts it to attributes.
        Any fields expected to be int (width, height, size, frames) will be attempted
        to be converted. Also, the keys of `data` serve as the attribute names, but
        with special action taken. Keys are split by the last underscore; anything prior
        becomes the attribute name, anything after becomes a sub-attribute. For example:
        fixed_width_downsampled will end up at `self.fixed_width.downsampled`
        """
        # Order matters :)
        process = ('original',
                   'fixed_width',
                   'fixed_height',
                   'fixed_width_downsampled',
                   'fixed_width_still',
                   'fixed_height_downsampled',
                   'fixed_height_still',
                   'downsized')

        for key in process:
            data = images.get(key)

            # Ignore empties
            if not data:
                continue

            parts = key.split('_')

            # attr/subattr style
            if len(parts) > 2:
                attr, subattr = '_'.join(parts[:-1]), parts[-1]
            else:
                attr, subattr = '_'.join(parts), None

            # Normalize data
            img = AttrDict(self._normalized(data))

            if subattr is None:
                setattr(self, attr, img)
            else:
                setattr(getattr(self, attr), subattr, img)

    def _normalized(self, data):
        """
        Does a normalization of sorts on image type data so that values
        that should be integers are converted from strings
        """
        int_keys = ('frames', 'width', 'height', 'size')

        for key in int_keys:
            if key not in data:
                continue

            try:
                data[key] = int(data[key])
            except ValueError:
                pass  # Ignored

        return data


class Giphy(object):

    """
    A python wrapper around the Giphy api. You should supply your own api
    key when using this class, but it will default to Giphy's public api
    key for testing. Note, that this might go away in the future, so don't
    use this for anything outside of testing/playing with the api.

    You can also supply a `strict` flag that will raise an exception if any
    api method does not return a result. Note that individual api methods
    also accept this flag if you would like more control over this behavior.
    """

    def __init__(self, api_key=GIPHY_PUBLIC_KEY, strict=False):
        # Warn if using public key
        if api_key == GIPHY_PUBLIC_KEY:
            warnings.warn('You are using the giphy public api key. This '
                          'should be used for testing only and may be '
                          'deactivated in the future. See '
                          'https://github.com/Giphy/GiphyAPI.')

        self.api_key = api_key
        self.strict = strict

    def _endpoint(self, name):
        return '/'.join((GIPHY_API_ENDPOINT, name))

    def _check_or_raise(self, meta):
        if meta.get('status') != 200:
            raise GiphyApiException(meta.get('error_message'))

    def _fetch(self, endpoint_name, **params):
        """
        Wrapper for making an api request from giphy
        """
        params['api_key'] = self.api_key

        resp = requests.get(self._endpoint(endpoint_name), params=params)
        resp.raise_for_status()

        data = resp.json()
        self._check_or_raise(data.get('meta', {}))

        return data

    def search(self, term=None, phrase=None, limit=DEFAULT_SEARCH_LIMIT,
               rating=None):
        """
        Search for gifs with a given word or phrase. Punctuation is ignored.
        By default, this will perform a `term` search. If you want to search
        by phrase, use the `phrase` keyword argument. What's the difference
        between `term` and `phrase` searches? Simple: a term search will
        return results matching any words given, whereas a phrase search will
        match all words.

        Note that this method is a GiphyImage generator that
        automatically handles api paging. Optionally accepts a limit that will
        terminate the generation after a specified number of results have been
        yielded. This defaults to 25 results; a None implies no limit

        :param term: Search term or terms
        :type term: string
        :param phrase: Search phrase
        :type phrase: string
        :param limit: Maximum number of results to yield
        :type limit: int
        :param rating: limit results to those rated (y,g, pg, pg-13 or r).
        :type rating: string
        """
        assert any((term, phrase)), 'You must supply a term or phrase to search'

        # Phrases should have dashes and not spaces
        if phrase:
            phrase = phrase.replace(' ', '-')

        results_yielded = 0  # Count how many things we yield
        page, per_page = 0, 25
        params = {'q': (term or phrase)}
        if rating:
            params.update({'rating': rating})
        fetch = partial(self._fetch, 'search', **params)

        # Generate results until we 1) run out of pages 2) reach a limit
        while True:
            data = fetch(offset=page, limit=per_page)
            page += per_page

            # Guard for empty results
            if not data['data']:
                raise StopIteration

            for item in data['data']:
                results_yielded += 1
                yield GiphyImage(item)

                if limit is not None and results_yielded >= limit:
                    raise StopIteration

            # Check yieled limit and whether or not there are more items
            if (page >= data['pagination']['total_count'] or
                    (limit is not None and results_yielded >= limit)):
                raise StopIteration

    def search_list(self, term=None, phrase=None, limit=DEFAULT_SEARCH_LIMIT,
                    rating=None):
        """
        Suppose you expect the `search` method to just give you a list rather
        than a generator. This method will have that effect. Equivalent to::

            >>> g = Giphy()
            >>> results = list(g.search('foo'))
        """
        return list(self.search(term=term, phrase=phrase, limit=limit,
                                rating=rating))

    def translate(self, term=None, phrase=None, strict=False, rating=None):
        """
        Retrieve a single image that represents a transalation of a term or
        phrase into an animated gif. Punctuation is ignored. By default, this
        will perform a `term` translation. If you want to translate by phrase,
        use the `phrase` keyword argument.

        :param term: Search term or terms
        :type term: string
        :param phrase: Search phrase
        :type phrase: string
        :param strict: Whether an exception should be raised when no results
        :type strict: boolean
        :param rating: limit results to those rated (y,g, pg, pg-13 or r).
        :type rating: string
        """
        assert any((term, phrase)), 'You must supply a term or phrase to search'

        # Phrases should have dashes and not spaces
        if phrase:
            phrase = phrase.replace(' ', '-')

        params = {'s': (term or phrase)}
        if rating:
            params.update({'rating': rating})
        resp = self._fetch('translate', **params)
        if resp['data']:
            return GiphyImage(resp['data'])
        elif strict or self.strict:
            raise GiphyApiException(
                "Term/Phrase '%s' could not be translated into a GIF" %
                (term or phrase))

    def trending(self, rating=None, limit=DEFAULT_SEARCH_LIMIT):
        """
        Retrieve GIFs currently trending online. The data returned mirrors
        that used to create The Hot 100 list of GIFs on Giphy.

        :param rating: limit results to those rated (y,g, pg, pg-13 or r).
        :type rating: string
        :param limit: Maximum number of results to yield
        :type limit: int
        """

        results_yielded = 0  # Count how many things we yield
        page, per_page = 0, 25
        params = {'rating': rating} if rating else {}
        fetch = partial(self._fetch, 'trending', **params)

        # Generate results until we 1) run out of pages 2) reach a limit
        while True:
            data = fetch(offset=page, limit=per_page)
            page += per_page

            # Guard for empty results
            if not data['data']:
                raise StopIteration

            for item in data['data']:
                results_yielded += 1
                yield GiphyImage(item)

                if limit is not None and results_yielded >= limit:
                    raise StopIteration

            # Check yieled limit and whether or not there are more items
            if (page >= data['pagination']['total_count'] or
                    (limit is not None and results_yielded >= limit)):
                raise StopIteration

    def trending_list(self, rating=None, limit=DEFAULT_SEARCH_LIMIT):
        """
        Suppose you expect the `trending` method to just give you a list rather
        than a generator. This method will have that effect. Equivalent to::

            >>> g = Giphy()
            >>> results = list(g.trending())
        """
        return list(self.trending(limit=limit, rating=rating))

    def gif(self, gif_id, strict=False):
        """
        Retrieves a specifc gif from giphy based on unique id

        :param gif_id: Unique giphy gif ID
        :type gif_id: string
        :param strict: Whether an exception should be raised when no results
        :type strict: boolean
        """
        resp = self._fetch(gif_id)

        if resp['data']:
            return GiphyImage(resp['data'])
        elif strict or self.strict:
            raise GiphyApiException(
                "GIF with ID '%s' could not be found" % gif_id)

    def screensaver(self, tag=None, strict=False):
        """
        Returns a random giphy image, optionally based on a search of a given tag.
        Note that this method will both query for a screensaver image and fetch the
        full details of that image (2 request calls)

        :param tag: Tag to retrieve a screensaver image
        :type tag: string
        :param strict: Whether an exception should be raised when no results
        :type strict: boolean
        """
        params = {'tag': tag} if tag else {}
        resp = self._fetch('screensaver', **params)

        if resp['data'] and resp['data']['id']:
            return self.gif(resp['data']['id'])
        elif strict or self.strict:
            raise GiphyApiException(
                "No screensaver GIF tagged '%s' found" % tag)

    # Alias
    random_gif = screensaver

    def upload(self, tags, file_path, username=None):
        """
        Uploads a gif from the filesystem to Giphy.

        :param tags: Tags to apply to the uploaded image
        :type tags: list
        :param file_path: Path at which the image can be found
        :type file_path: string
        :param username: Your channel username if not using public API key
        """
        params = {
            'api_key': self.api_key,
            'tags': ','.join(tags)
        }
        if username is not None:
            params['username'] = username

        with open(file_path, 'rb') as f:
            resp = requests.post(
                GIPHY_UPLOAD_ENDPOINT, params=params, files={'file': f})

        resp.raise_for_status()

        data = resp.json()
        self._check_or_raise(data.get('meta', {}))

        return self.gif(data['data']['id'])


def search(term=None, phrase=None, limit=DEFAULT_SEARCH_LIMIT,
           api_key=GIPHY_PUBLIC_KEY, strict=False, rating=None):
    """
    Shorthand for creating a Giphy api wrapper with the given api key
    and then calling the search method. Note that this will return a generator
    """
    return Giphy(api_key=api_key, strict=strict).search(
        term=term, phrase=phrase, limit=limit, rating=rating)


def search_list(term=None, phrase=None, limit=DEFAULT_SEARCH_LIMIT,
                api_key=GIPHY_PUBLIC_KEY, strict=False, rating=None):
    """
    Shorthand for creating a Giphy api wrapper with the given api key
    and then calling the search_list method.
    """
    return Giphy(api_key=api_key, strict=strict).search_list(
        term=term, phrase=phrase, limit=limit, rating=rating)


def translate(term=None, phrase=None, api_key=GIPHY_PUBLIC_KEY, strict=False,
              rating=None):
    """
    Shorthand for creating a Giphy api wrapper with the given api key
    and then calling the translate method.
    """
    return Giphy(api_key=api_key, strict=strict).translate(
        term=term, phrase=phrase, rating=rating)


def trending(limit=DEFAULT_SEARCH_LIMIT, api_key=GIPHY_PUBLIC_KEY,
             strict=False, rating=None):
    """
    Shorthand for creating a Giphy api wrapper with the given api key
    and then calling the trending method. Note that this will return
    a generator
    """
    return Giphy(api_key=api_key, strict=strict).trending(
        limit=limit, rating=rating)


def trending_list(limit=DEFAULT_SEARCH_LIMIT, api_key=GIPHY_PUBLIC_KEY,
                  strict=False, rating=None):
    """
    Shorthand for creating a Giphy api wrapper with the given api key
    and then calling the trending_list method.
    """
    return Giphy(api_key=api_key, strict=strict).trending_list(
        limit=limit, rating=rating)


def gif(gif_id, api_key=GIPHY_PUBLIC_KEY, strict=False):
    """
    Shorthand for creating a Giphy api wrapper with the given api key
    and then calling the gif method.
    """
    return Giphy(api_key=api_key, strict=strict).gif(gif_id)


def screensaver(tag=None, api_key=GIPHY_PUBLIC_KEY, strict=False):
    """
    Shorthand for creating a Giphy api wrapper with the given api key
    and then calling the screensaver method.
    """
    return Giphy(api_key=api_key, strict=strict).screensaver(tag=tag)

# Alias
random_gif = screensaver


def upload(tags, file_path, username=None, api_key=GIPHY_PUBLIC_KEY,
           strict=False):
    """
    Shorthand for creating a Giphy api wrapper with the given api key
    and then calling the upload method.
    """
    return Giphy(api_key=api_key, strict=strict).upload(
        tags, file_path, username)
