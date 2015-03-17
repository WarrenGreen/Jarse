//set up gulp and gulp packages 

var gulp = require('gulp');
var sass = require('gulp-sass');
var lint = require('gulp-jshint');
var rename = require('gulp-rename');

// path constants (use glob notation)
var ALL_SCSS = './scss/*'; 
var ALL_JS = './js/**/*.js'; 
var ALL_CSS = './css/**/*.css';

var SOURCE_JS = './js/core/*.js';

var DEST_CSS_LIB = './css/lib/';
var DEST_JS_LIB = './js/lib/';
var DEST_CSS_CORE = './css/core/';
var DEST_JS_CORE = './js/core/';

//bower-installed packages
var BOWER_JQUERY = './bower_components/jquery/dist/jquery.js';
var BOWER_BOOTSTRAP_CSS = './bower_components/bootstrap/dist/css/bootstrap.min.css';
var BOWER_BOOTSTRAP_JS = './bower_components/bootstrap/dist/js/bootstrap.min.js'; 
var BOWER_NORMALIZE = './bower_components/normalize.css/normalize.css';

gulp.task('sass', function(){
	return gulp.src(ALL_SCSS)
		.pipe(sass())
		.pipe(gulp.dest(DEST_CSS_CORE));
});

gulp.task('moveBowerJs', function(){
	return gulp.src([BOWER_JQUERY, BOWER_BOOTSTRAP_JS])
			.pipe(gulp.dest(DEST_JS_LIB));
});

gulp.task('moveBowerCss', function(){
	return gulp.src([BOWER_BOOTSTRAP_CSS, BOWER_NORMALIZE])
		.pipe(gulp.dest(DEST_CSS_LIB));
});

gulp.task('move', ['moveBowerJs', 'moveBowerCss']);

gulp.task('lint', function(){
	return gulp.src(SOURCE_JS)
		.pipe(lint())
		.pipe(lint.reporter('default'));
});

gulp.task('watch', function(){
	gulp.watch(ALL_SCSS, ['sass']);
	gulp.watch(SOURCE_JS, ['lint']);
});

gulp.task('default', ['move', 'sass', 'lint']);
