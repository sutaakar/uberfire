/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.uberfire.ext.preferences.client.admin;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.jboss.errai.ioc.client.api.ManagedInstance;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;
import org.uberfire.client.mvp.UberElement;
import org.uberfire.ext.preferences.client.admin.category.AdminPageCategoryPresenter;
import org.uberfire.ext.preferences.client.admin.page.AdminPage;
import org.uberfire.workbench.events.NotificationEvent;

@WorkbenchScreen(identifier = AdminPagePresenter.IDENTIFIER)
public class AdminPagePresenter {

    public static final String IDENTIFIER = "AdminPagePresenter";

    public interface View extends UberElement<AdminPagePresenter> {

        void add( final AdminPageCategoryPresenter.View categoryView );

        String getTitle();
    }

    private final View view;

    private final AdminPage adminPage;

    private final ManagedInstance<AdminPageCategoryPresenter> categoryPresenterProvider;

    private final Event<NotificationEvent> notification;

    @Inject
    public AdminPagePresenter( final View view,
                               final AdminPage adminPage,
                               final ManagedInstance<AdminPageCategoryPresenter> categoryPresenterProvider,
                               final Event<NotificationEvent> notification ) {
        this.view = view;
        this.adminPage = adminPage;
        this.categoryPresenterProvider = categoryPresenterProvider;
        this.notification = notification;
    }

    @PostConstruct
    public void init() {
        view.init( this );

        adminPage.getToolsByCategory().forEach( ( category, adminTools ) -> {
            AdminPageCategoryPresenter categoryPresenter = categoryPresenterProvider.get();
            categoryPresenter.setup( adminTools );
            view.add( categoryPresenter.getView() );
        } );
    }

    @WorkbenchPartTitle
    public String getTitle() {
        return view.getTitle();
    }

    @WorkbenchPartView
    public View getView() {
        return view;
    }
}
