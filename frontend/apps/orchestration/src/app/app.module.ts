import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { NxWelcomeComponent } from './nx-welcome.component';
import { RouterModule } from '@angular/router';
import { loadRemoteModule } from '@angular-architects/module-federation';

@NgModule({
  declarations: [AppComponent, NxWelcomeComponent],
  imports: [
    BrowserModule,
    RouterModule.forRoot([
      {
        path: 'test',
        component: NxWelcomeComponent
      },
      {
        path: 'object-store',
        loadChildren: () =>
          loadRemoteModule({
            type: 'module',
            remoteEntry: 'http://192.168.144.2:8889/remoteEntry.js',
            exposedModule: './Module',
          })
            .then(m =>  m.RemoteEntryModule)
      },
    ], { initialNavigation: 'enabledBlocking' }),
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
