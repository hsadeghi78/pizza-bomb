import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'party',
        data: { pageTitle: 'bombApp.party.home.title' },
        loadChildren: () => import('./party/party.module').then(m => m.PartyModule),
      },
      {
        path: 'branch',
        data: { pageTitle: 'bombApp.branch.home.title' },
        loadChildren: () => import('./branch/branch.module').then(m => m.BranchModule),
      },
      {
        path: 'criticism',
        data: { pageTitle: 'bombApp.criticism.home.title' },
        loadChildren: () => import('./criticism/criticism.module').then(m => m.CriticismModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
